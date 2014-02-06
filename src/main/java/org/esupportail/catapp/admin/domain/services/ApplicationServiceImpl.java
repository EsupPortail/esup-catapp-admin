package org.esupportail.catapp.admin.domain.services;

import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fj.data.Array.array;
import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;


public class ApplicationServiceImpl extends AbstractService implements IApplicationService {

    @Inject
    private WebTarget restApplicationsService;

    @Override
    public ApplicationDTO getApplication(final String code) throws InterruptedException {
        try {
            return restApplicationsService.path(toLowerCase.f(code)).request().get(ApplicationDTO.class);
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public fj.data.List<ApplicationDTO> getApplications() throws InterruptedException {
        try {
            return array(restApplicationsService.request().get(ApplicationDTO[].class)).toList();
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public ApplicationDTO addApplication(final ApplicationDTO applicationDTO)
            throws InterruptedException, CrudException {
        try {
            final Response response = restApplicationsService.request()
                    .post(entity(applicationDTO, MediaType.APPLICATION_JSON_TYPE));
            final Response.StatusType statusInfo = response.getStatusInfo();

            if (!(CREATED == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible d'ajouter l'application [%s]", applicationDTO.getCode()));
            }
            return getApplication(applicationDTO.getCode());
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public ApplicationDTO updateApplication(final ApplicationDTO applicationDTO)
            throws InterruptedException, CrudException {
        try {
            final Response response = restApplicationsService.request()
                    .put(entity(applicationDTO, MediaType.APPLICATION_JSON_TYPE));
            final Response.StatusType statusInfo = response.getStatusInfo();

            if (!(OK == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible de modifier l'application [%s]", applicationDTO.getCode()));
            }
            return applicationDTO;
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void deleteApplication(final String code) throws InterruptedException, CrudException {
        try {
            final Response response = restApplicationsService.path(toLowerCase.f(code)).request().delete();
            final Response.StatusType statusInfo = response.getStatusInfo();

            if (!(OK == statusInfo.getStatusCode())) {
                throw new CrudException(format("Impossible de supprimer l'application [%s]", code));
            }
        } catch (ProcessingException | WebApplicationException e) {
            throw new InterruptedException(e.getMessage());
        }

    }
}
