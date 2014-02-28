package org.esupportail.catapp.admin.domain.services.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import fj.*;
import fj.data.List;
import fj.data.Stream;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static fj.Equal.stringEqual;
import static fj.Function.curry;
import static fj.Ord.ord;
import static fj.Ord.stringOrd;
import static fj.P.p;
import static fj.data.Array.array;
import static fj.data.Option.fromNull;
import static fj.data.Tree.unfoldTree;
import static java.lang.String.format;
import static org.esupportail.catapp.admin.domain.beans.DomaineDTO.domaineDTO;

public class MockDomaineService implements IDomaineService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String jsonDomains = "[{\"code\":\"ROOT\",\"caption\":\"Domaine racine\",\"parent\":null,\"domains\":[\"ENV-TRAVAIL\",\"OUTILS-COM\",\"APPS-METIER\"],\"applications\":[]},{\"code\":\"ENV-TRAVAIL\",\"caption\":\"Environnement de travail\",\"parent\":\"ROOT\",\"domains\":[],\"applications\":[]},{\"code\":\"OUTILS-COM\",\"caption\":\"Outils de communication\",\"parent\":\"ROOT\",\"domains\":[],\"applications\":[]},{\"code\":\"APPS-METIER\",\"caption\":\"Applications métier\",\"parent\":\"ROOT\",\"domains\":[\"PILOTAGE\",\"GRH\",\"GEE\",\"GFC\",\"GP\"],\"applications\":[]},{\"code\":\"PILOTAGE\",\"caption\":\"Pilotage\",\"parent\":\"APPS-METIER\",\"domains\":[],\"applications\":[]},{\"code\":\"GRH\",\"caption\":\"Gestion Ressources Humaines\",\"parent\":\"APPS-METIER\",\"domains\":[],\"applications\":[]},{\"code\":\"GEE\",\"caption\":\"Gestion des enseignements et étudiants\",\"parent\":\"APPS-METIER\",\"domains\":[],\"applications\":[]},{\"code\":\"GFC\",\"caption\":\"Gestion financière et comptable\",\"parent\":\"APPS-METIER\",\"domains\":[],\"applications\":[\"SIFAC-P04\",\"SIFAC-T04\",\"SIFAC-DOC\"]},{\"code\":\"GP\",\"caption\":\"Gestion patrimoine\",\"parent\":\"APPS-METIER\",\"domains\":[],\"applications\":[]}]";

    @Override
    public boolean exists(final String code) throws InterruptedException {
        return getList().exists(new F<DomaineDTO, Boolean>() {
            public Boolean f(DomaineDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        });
    }

    @Override
    public DomaineDTO getOne(final String code) throws InterruptedException {
        return getList().find(new F<DomaineDTO, Boolean>() {
            public Boolean f(DomaineDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        }).toNull();
    }

    @Override
    public List<DomaineDTO> getList() throws InterruptedException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return array(mapper.readValue(jsonDomains, DomaineDTO[].class)).toList();
        } catch (IOException e) {
            return List.nil();
        }
    }

    @Override
    public void add(final DomaineDTO dto) throws InterruptedException, CrudException {
        log.info("DomainDTO in add method {}", dto);
        if (exists(dto.getCode())) {
            throw new CrudException("ERROR.VALIDATION.UNIQUE");
        }
        // do nothing else
    }

    @Override
    public void update(final DomaineDTO dto) throws InterruptedException, CrudException {
        log.info("DomainDTO in update method {}", dto);
        //do nothing
    }

    @Override
    public void delete(final String code) throws InterruptedException, CrudException {
        // do nothing
    }

    @Override
    public Tree<DomaineDTO> treeDomaines() throws InterruptedException {
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
                return p(root, p(children._1().sort(ord(curry(ordering)))));
            }
        });

        return treeFunc.f(domaines.orHead(p(domaineDTO("", "", ""))));
    }

    public static final F2<DomaineDTO, DomaineDTO, Ordering> ordering = new F2<DomaineDTO, DomaineDTO, Ordering>() {
        public Ordering f(final DomaineDTO a, final DomaineDTO b) {
            if(a.getParent() == null || b.getParent() == null) {
                return Ordering.GT;
            }
            final String s1 = format("%s %s", a.getCaption(), a.getCode()).trim().toLowerCase();
            final String s2 = format("%s %s", b.getCaption(), b.getCode()).trim().toLowerCase();
            return stringOrd.compare(s1, s2);
        }
    };
}
