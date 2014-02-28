package org.esupportail.catapp.admin.domain.services;

import fj.*;
import fj.data.List;
import fj.data.Stream;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.enums.Exists;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fj.Equal.stringEqual;
import static fj.Function.curry;
import static fj.Ord.stringOrd;
import static fj.P.p;
import static fj.data.Array.array;
import static fj.data.Option.fromNull;
import static fj.data.Tree.unfoldTree;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.Response.StatusType;
import static org.esupportail.catapp.admin.domain.beans.DomaineDTO.domaineDTO;

public class DomaineServiceImpl extends AbstractService implements IDomaineService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private WebTarget restDomainesService;


    @Override
    public boolean exists(final String code) throws InterruptedException {
        try {
            return restDomainesService.path(code).path("exists").request().get(Exists.class).value();
        } catch (Exception e) {
            log.error("error in exists", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public DomaineDTO getOne(final String code) throws InterruptedException {

        try {
            return restDomainesService.path(toLowerCase.f(code)).request().get(DomaineDTO.class);
        } catch (Exception e) {
            log.error("error in getOne", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public List<DomaineDTO> getList() throws InterruptedException {
        try {
            return array(restDomainesService.request().get(DomaineDTO[].class))
                    .toList();
        } catch (Exception e) {
            log.error("error in getList", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void add(final DomaineDTO dto) throws InterruptedException, CrudException {
        log.debug("DomainDTO in add method {}", dto);
        try {
            if (exists(dto.getCode())) {
                throw new CrudException("Violation de la contrainte d'unicité");
            }

            final Response response = restDomainesService.request()
                    .post(entity(dto, MediaType.APPLICATION_JSON_TYPE));
            final StatusType statusInfo = response.getStatusInfo();

            if (!(CREATED == statusInfo.getStatusCode())) {
                log.error("response status expected {} but got {}", CREATED, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible d'ajouter le domaine [%s]", dto.getCode()));
            }
        } catch (Exception e) {
            log.error("error in add", e);
            if (e instanceof CrudException) {
                throw e;
            }
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void update(final DomaineDTO dto) throws InterruptedException, CrudException {
        log.debug("DomainDTO in update method {}", dto);
        try {
            final Response response = restDomainesService.path(dto.getCode()).request()
                    .put(entity(dto, MediaType.APPLICATION_JSON_TYPE));
            final StatusType statusInfo = response.getStatusInfo();
            log.debug("response status {}", statusInfo);
            if (!(OK == statusInfo.getStatusCode())) {
                log.error("response status expected {} but got {}", OK, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible de modifier le domaine [%s]", dto.getCode()));
            }
        } catch (Exception e) {
            log.error("error in update", e);
            if (e instanceof CrudException) {
                throw e;
            }
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void delete(final String code) throws InterruptedException, CrudException {
        try {
            final Response response = restDomainesService.path(toLowerCase.f(code)).request().delete();
            final StatusType statusInfo = response.getStatusInfo();

            log.debug("Response in delete method {}", response);

            if (!(NO_CONTENT == statusInfo.getStatusCode())) {
                log.error("response status expected {} but got {}", NO_CONTENT, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible de supprimer le domaine [%s]", code));
            }
        } catch (Exception e) {
            log.error("error in delete", e);
            if (e instanceof CrudException) {
                throw e;
            }
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public Tree<DomaineDTO> treeDomaines() throws InterruptedException {
        try {
            final Stream<DomaineDTO> domaines = getList().toStream();

            final F<DomaineDTO, Tree<DomaineDTO>> treeFunc = unfoldTree(new F<DomaineDTO, P2<DomaineDTO, P1<Stream<DomaineDTO>>>>() {
                public P2<DomaineDTO, P1<Stream<DomaineDTO>>> f(final DomaineDTO root) {
                    final P1<Stream<DomaineDTO>> children = new P1<Stream<DomaineDTO>>() {
                        public Stream<DomaineDTO> _1() {
                            return domaines.filter(new F<DomaineDTO, Boolean>() {
                                public Boolean f(DomaineDTO child) {
                                    return fromNull(child.getParent()).exists(stringEqual.eq().f(root.getCode()));
                                }
                            });
                        }
                    };
                    return p(root, children);
                }
            });
            return treeFunc.f(domaines.orHead(p(domaineDTO("","",""))));
        } catch (InterruptedException e) {
            log.error("error in treeDomaines", e);
            throw e;
        }
    }

    public static final F2<DomaineDTO, DomaineDTO, Ordering> ordering = new F2<DomaineDTO, DomaineDTO, Ordering>() {
        public Ordering f(final DomaineDTO a, final DomaineDTO b) {
            final String s1 = format("%s %s", a.getCaption(), a.getCode()).trim().toLowerCase();
            final String s2 = format("%s %s", b.getCaption(), b.getCode()).trim().toLowerCase();
            return stringOrd.compare(s1, s2);
        }
    };
}
