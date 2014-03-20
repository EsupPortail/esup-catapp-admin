package org.esupportail.catapp.admin.domain.services;

import fj.F2;
import fj.Ordering;
import fj.data.List;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.beans.Exists;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fj.Function.curry;
import static fj.Ord.ord;
import static fj.Ord.stringOrd;
import static fj.data.Array.array;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;


public class ApplicationServiceImpl extends AbstractService implements IApplicationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private WebTarget restApplicationsService;

    @Override
    public boolean exists(final String code) throws InterruptedException {
        try {
            return restApplicationsService.path(code).path("exists").request().get(Exists.class).exists();
        } catch (Exception e) {
            log.error("error in exists", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public ApplicationDTO getOne(final String code) throws InterruptedException {
        try {
            return restApplicationsService.path(code).request().get(ApplicationDTO.class);
        } catch (Exception e) {
            log.error("error in getOne", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationDTO> getList() throws InterruptedException {
        try {
            return array(restApplicationsService.request().get(ApplicationDTO[].class))
                    .toList().sort(ord(curry(ordering)));
        } catch (Exception e) {
            log.error("error in getList", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void add(final ApplicationDTO dto) throws InterruptedException, CrudException {
        log.debug("ApplicationDTO in add method {}", dto);
        try {
            if (exists(dto.getCode())) {
                throw new CrudException("Violation de la contrainte d'unicité");
            }

            final Response response = restApplicationsService.request()
                    .post(entity(dto, MediaType.APPLICATION_JSON_TYPE));
            final Response.StatusType statusInfo = response.getStatusInfo();

            if (!(CREATED == statusInfo.getStatusCode())) {
                log.error("response status expected {} but got {}", CREATED, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible d'ajouter l'application [%s]", dto.getCode()));
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
    public void update(final ApplicationDTO dto) throws InterruptedException, CrudException {
        log.debug("ApplicationDTO in update method {}", dto);
        try {
            final Response response = restApplicationsService.path(dto.getCode()).request()
                    .put(entity(dto, MediaType.APPLICATION_JSON_TYPE));
            final Response.StatusType statusInfo = response.getStatusInfo();

            if (!(OK == statusInfo.getStatusCode())) {
                log.error("response status expected {} but got {}", OK, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible de modifier l'application [%s]", dto.getCode()));
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
            final Response response = restApplicationsService.path(toLowerCase.f(code)).request().delete();
            final Response.StatusType statusInfo = response.getStatusInfo();
            log.debug("Response in delete method {}", response);

            if (NO_CONTENT != statusInfo.getStatusCode()) {
                log.error("response status expected {} but got {}", NO_CONTENT, statusInfo.getStatusCode());
                throw new CrudException(format("Impossible de supprimer l'application [%s]", code));
            }
        } catch (Exception e) {
            log.error("error in delete", e);
            if (e instanceof CrudException) {
                throw e;
            }
            throw new InterruptedException(e.getMessage());
        }
    }

    public static final F2<ApplicationDTO, ApplicationDTO, Ordering> ordering = new F2<ApplicationDTO, ApplicationDTO, Ordering>() {
        public Ordering f(final ApplicationDTO a, final ApplicationDTO b) {
            final String s1 = format("%s %s", a.getTitle(), a.getCaption()).trim().toLowerCase();
            final String s2 = format("%s %s", b.getTitle(), b.getCaption()).trim().toLowerCase();
            return stringOrd.compare(s1, s2);
        }
    };
}
