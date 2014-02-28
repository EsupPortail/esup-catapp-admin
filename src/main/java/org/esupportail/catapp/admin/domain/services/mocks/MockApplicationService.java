package org.esupportail.catapp.admin.domain.services.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import fj.F;
import fj.F2;
import fj.Ordering;
import fj.data.List;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static fj.Function.curry;
import static fj.Ord.ord;
import static fj.Ord.stringOrd;
import static fj.data.Array.array;
import static java.lang.String.format;

public class MockApplicationService implements IApplicationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String jsonApplications = "[{\"code\":\"SIFAC-P04\",\"title\":\"Clients légers - Sifac production\",\"caption\":\"mandant 500\",\"description\":\"\",\"url\":\"\",\"group\":\"\",\"domains\":[\"GFC\"],\"activation\":\"Activated\"},{\"code\":\"SIFAC-T04\",\"title\":\"Clients légers - Sifac test\",\"caption\":\"mandant 310\",\"description\":\"\",\"url\":\"\",\"group\":\"\",\"domains\":[\"GFC\"],\"activation\":\"Activated\"},{\"code\":\"SIFAC-DOC\",\"title\":\"Documentations et procédures Sifac\",\"caption\":\"\",\"description\":\"\",\"url\":\"\",\"group\":\"\",\"domains\":[\"GFC\"],\"activation\":\"Activated\"}]";

    @Override
    public boolean exists(final String code) throws InterruptedException {
        return getList().exists(new F<ApplicationDTO, Boolean>() {
            public Boolean f(ApplicationDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        });
    }

    @Override
    public ApplicationDTO getOne(final String code) throws InterruptedException {
        return getList().find(new F<ApplicationDTO, Boolean>() {
            public Boolean f(ApplicationDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        }).toNull();
    }

    @Override
    public List<ApplicationDTO> getList() throws InterruptedException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return array(mapper.readValue(jsonApplications, ApplicationDTO[].class))
                    .toList().sort(ord(curry(ordering)));
        } catch (IOException e) {
            return List.nil();
        }
    }

    @Override
    public void add(final ApplicationDTO dto) throws InterruptedException, CrudException {
        log.info("ApplicationDTO in update method {}", dto);
        if (exists(dto.getCode())) {
            throw new CrudException("ERROR.VALIDATION.UNIQUE");
        }
        // do nothing else
    }

    @Override
    public void update(final ApplicationDTO dto) throws InterruptedException, CrudException {
        log.info("ApplicationDTO in update method {}", dto);
        //do nothing
    }

    @Override
    public void delete(final String code) throws InterruptedException, CrudException {
        //do nothing
    }

    public static final F2<ApplicationDTO, ApplicationDTO, Ordering> ordering = new F2<ApplicationDTO, ApplicationDTO, Ordering>() {
        public Ordering f(final ApplicationDTO a, final ApplicationDTO b) {
            final String s1 = format("%s %s", a.getTitle(), a.getCaption()).trim().toLowerCase();
            final String s2 = format("%s %s", b.getTitle(), b.getCaption()).trim().toLowerCase();
            return stringOrd.compare(s1, s2);
        }
    };
}
