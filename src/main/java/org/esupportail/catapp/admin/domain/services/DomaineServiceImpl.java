package org.esupportail.catapp.admin.domain.services;

import fj.F;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fj.Equal.stringEqual;
import static fj.P.p;
import static fj.data.Array.array;
import static fj.data.Tree.unfoldTree;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.Response.StatusType;

public class DomaineServiceImpl extends AbstractService implements IDomaineService {

    @Inject
    private WebTarget restDomainesService;


    @Override
    public DomaineDTO getDomaine(final String code) throws InterruptedException {

        try {
            return restDomainesService.path(toLowerCase.f(code)).request().get(DomaineDTO.class);
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public List<DomaineDTO> getDomaines() throws InterruptedException {
        try {
            return array(restDomainesService.request().get(DomaineDTO[].class)).toList();
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public DomaineDTO addDomaine(final DomaineDTO domaineDTO) throws InterruptedException, CrudException {
        try {
            final Response response = restDomainesService.request()
                    .post(entity(domaineDTO, MediaType.APPLICATION_JSON_TYPE));
            final StatusType statusInfo = response.getStatusInfo();

            if (!(CREATED == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible d'ajouter le domaine [%s]", domaineDTO.getCode()));
            }
            return getDomaine(domaineDTO.getCode());
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public DomaineDTO updateDomaine(final DomaineDTO domaineDTO) throws InterruptedException, CrudException {
        try {
            final Response response = restDomainesService.request()
                    .put(entity(domaineDTO, MediaType.APPLICATION_JSON_TYPE));
            final StatusType statusInfo = response.getStatusInfo();

            if (!(OK == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible de modifier le domaine [%s]", domaineDTO.getCode()));
            }
            return domaineDTO;
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void deleteDomaine(final String code) throws InterruptedException, CrudException {
        try {
            final Response response = restDomainesService.path(toLowerCase.f(code)).request().delete();
            final StatusType statusInfo = response.getStatusInfo();

            if (!(OK == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible de supprimer le domaine [%s]", code));
            }
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public Tree<DomaineDTO> treeDomaines() throws InterruptedException {
        try {
            final List<DomaineDTO> allDomains = getDomaines();
            final Option<DomaineDTO> optRoot = allDomains.find(new F<DomaineDTO, Boolean>() {
                public Boolean f(DomaineDTO dto) {
                    return dto.getParent().isNone();
                }
            });
            if (optRoot.isNone()) {
                throw new InterruptedException("Aucune racine présente pour les domaines !");
            }
            final DomaineDTO root = optRoot.some();
            final Stream<DomaineDTO> domaines = allDomains.toStream().removeAll(new F<DomaineDTO, Boolean>() {
                public Boolean f(DomaineDTO dto) {
                    return dto.getCode().equals(root.getCode());
                }
            });

            final F<DomaineDTO, Tree<DomaineDTO>> treeFunc = unfoldTree(new F<DomaineDTO, P2<DomaineDTO, P1<Stream<DomaineDTO>>>>() {
                public P2<DomaineDTO, P1<Stream<DomaineDTO>>> f(final DomaineDTO root) {
                    final P1<Stream<DomaineDTO>> children = new P1<Stream<DomaineDTO>>() {
                        public Stream<DomaineDTO> _1() {
                            return domaines.filter(new F<DomaineDTO, Boolean>() {
                                public Boolean f(DomaineDTO child) {
                                    return child.getParent().exists(stringEqual.eq().f(root.getCode()));
                                }
                            });
                        }
                    };
                    return p(root, children);
                }
            });

            return treeFunc.f(domaines.orHead(p(root)));
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        }
    }
}
