package org.esupportail.catapp.admin.domain.services.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import fj.F;
import fj.data.List;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IDomaineService;

import java.io.IOException;

import static fj.data.Array.array;

public class MockDomaineService implements IDomaineService {

    private final String jsonDomains = "[{\"pk\":1,\"code\":\"ROOT\",\"libelle\":\"Domaine racine\",\"parent\":null,\"domaines\":[\"ENV-TRAVAIL\",\"OUTILS-COM\",\"APPS-METIER\"],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[{\"rel\":\"DOM:ENV-TRAVAIL\",\"link\":\"http://localhost:8081/domains/env-travail\"},{\"rel\":\"DOM:OUTILS-COM\",\"link\":\"http://localhost:8081/domains/outils-com\"},{\"rel\":\"DOM:APPS-METIER\",\"link\":\"http://localhost:8081/domains/apps-metier\"}]},{\"pk\":2,\"code\":\"ENV-TRAVAIL\",\"libelle\":\"Environnement de travail\",\"parent\":\"ROOT\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]},{\"pk\":3,\"code\":\"OUTILS-COM\",\"libelle\":\"Outils de communication\",\"parent\":\"ROOT\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]},{\"pk\":4,\"code\":\"APPS-METIER\",\"libelle\":\"Applications métier\",\"parent\":\"ROOT\",\"domaines\":[\"PILOTAGE\",\"GRH\",\"GEE\",\"GFC\",\"GP\"],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[{\"rel\":\"DOM:PILOTAGE\",\"link\":\"http://localhost:8081/domains/pilotage\"},{\"rel\":\"DOM:GRH\",\"link\":\"http://localhost:8081/domains/grh\"},{\"rel\":\"DOM:GEE\",\"link\":\"http://localhost:8081/domains/gee\"},{\"rel\":\"DOM:GFC\",\"link\":\"http://localhost:8081/domains/gfc\"},{\"rel\":\"DOM:GP\",\"link\":\"http://localhost:8081/domains/gp\"}]},{\"pk\":5,\"code\":\"PILOTAGE\",\"libelle\":\"Pilotage\",\"parent\":\"APPS-METIER\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]},{\"pk\":6,\"code\":\"GRH\",\"libelle\":\"Gestion Ressources Humaines\",\"parent\":\"APPS-METIER\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]},{\"pk\":7,\"code\":\"GEE\",\"libelle\":\"Gestion des enseignements et étudiants\",\"parent\":\"APPS-METIER\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]},{\"pk\":8,\"code\":\"GFC\",\"libelle\":\"Gestion financière et comptable\",\"parent\":\"APPS-METIER\",\"domaines\":[],\"applications\":[\"SIFAC-P04\",\"SIFAC-T04\",\"SIFAC-DOC\"],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[{\"rel\":\"APP:SIFAC-P04\",\"link\":\"http://localhost:8081/domains/sifac-p04\"},{\"rel\":\"APP:SIFAC-T04\",\"link\":\"http://localhost:8081/domains/sifac-t04\"},{\"rel\":\"APP:SIFAC-DOC\",\"link\":\"http://localhost:8081/domains/sifac-doc\"}]},{\"pk\":9,\"code\":\"GP\",\"libelle\":\"Gestion patrimoine\",\"parent\":\"APPS-METIER\",\"domaines\":[],\"applications\":[],\"creation\":\"2014-01-01T00:00:00Z\",\"maj\":\"2014-01-01T00:00:00Z\",\"links\":[]}]";

    @Override
    public DomaineDTO getDomaine(final String code) throws InterruptedException {
        return getDomaines().find(new F<DomaineDTO, Boolean>() {
            public Boolean f(DomaineDTO dto) {
                return dto.getCode().equalsIgnoreCase(code);
            }
        }).toNull();
    }

    @Override
    public List<DomaineDTO> getDomaines() throws InterruptedException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return array(mapper.readValue(jsonDomains, DomaineDTO[].class)).toList();
        } catch (IOException e) {
            return List.nil();
        }
    }

    @Override
    public DomaineDTO addDomaine(final DomaineDTO domaineDTO) throws InterruptedException, CrudException {
        return domaineDTO;
    }

    @Override
    public DomaineDTO updateDomaine(final DomaineDTO domaineDTO) throws InterruptedException, CrudException {
        return domaineDTO;
    }

    @Override
    public void deleteDomaine(final String code) throws InterruptedException, CrudException {
        // do nothing
    }

    @Override
    public Tree<DomaineDTO> treeDomaines() throws InterruptedException {
        return Tree.leaf(getDomaine("ROOT"));
    }

}
