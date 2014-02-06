package org.esupportail.catapp.admin.domain.services;

import fj.data.List;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;


public interface IApplicationService {

    ApplicationDTO getApplication(String code) throws InterruptedException;

    List<ApplicationDTO> getApplications() throws InterruptedException;

    ApplicationDTO addApplication(ApplicationDTO applicationDTO) throws InterruptedException, CrudException;

    ApplicationDTO updateApplication(ApplicationDTO applicationDTO) throws InterruptedException, CrudException;

    void deleteApplication(String code) throws InterruptedException, CrudException;
}
