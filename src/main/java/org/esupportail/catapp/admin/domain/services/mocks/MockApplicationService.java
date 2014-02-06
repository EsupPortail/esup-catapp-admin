package org.esupportail.catapp.admin.domain.services.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import fj.F;
import fj.data.List;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IApplicationService;

import java.io.IOException;

import static fj.data.Array.array;

public class MockApplicationService implements IApplicationService {

    private final String jsonApplications = "[{\"pk\":1,\"code\":\"SIFAC-P04\",\"titre\":\"Clients légers - Sifac production\",\"libelle\":\"mandant 500\",\"description\":\"\",\"url\":\"\",\"groupe\":\"\",\"domaines\":[\"GFC\"],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"visible\":true,\"links\":[{\"rel\":\"DOM:GFC\",\"link\":\"http://localhost:8081/domains/gfc\"}]},{\"pk\":2,\"code\":\"SIFAC-T04\",\"titre\":\"Clients légers - Sifac test\",\"libelle\":\"mandant 310\",\"description\":\"\",\"url\":\"\",\"groupe\":\"\",\"domaines\":[\"GFC\"],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"visible\":true,\"links\":[{\"rel\":\"DOM:GFC\",\"link\":\"http://localhost:8081/domains/gfc\"}]},{\"pk\":3,\"code\":\"SIFAC-DOC\",\"titre\":\"Documentations et procédures Sifac\",\"libelle\":\"\",\"description\":\"\",\"url\":\"\",\"groupe\":\"\",\"domaines\":[\"GFC\"],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"visible\":true,\"links\":[{\"rel\":\"DOM:GFC\",\"link\":\"http://localhost:8081/domains/gfc\"}]}]";

    @Override
    public ApplicationDTO getApplication(final String code) throws InterruptedException {
        return getApplications().find(new F<ApplicationDTO, Boolean>() {
            public Boolean f(ApplicationDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        }).toNull();
    }

    @Override
    public fj.data.List<ApplicationDTO> getApplications() throws InterruptedException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return array(mapper.readValue(jsonApplications, ApplicationDTO[].class)).toList();
        } catch (IOException e) {
            return List.nil();
        }
    }

    @Override
    public ApplicationDTO addApplication(final ApplicationDTO applicationDTO) throws InterruptedException, CrudException {
        return applicationDTO;
    }

    @Override
    public ApplicationDTO updateApplication(final ApplicationDTO applicationDTO) throws InterruptedException, CrudException {
        return applicationDTO;
    }

    @Override
    public void deleteApplication(final String code) throws InterruptedException, CrudException {
        //do nothing
    }
}
