package org.esupportail.catapp.admin.domain.services;


import fj.data.List;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;


public interface IDomaineService {

    DomaineDTO getDomaine(String code) throws InterruptedException;

    List<DomaineDTO> getDomaines() throws InterruptedException;

    DomaineDTO addDomaine(DomaineDTO domaineDTO) throws InterruptedException, CrudException;

    DomaineDTO updateDomaine(DomaineDTO domaineDTO) throws InterruptedException, CrudException;

    void deleteDomaine(String code) throws InterruptedException, CrudException;

    Tree<DomaineDTO> treeDomaines() throws InterruptedException;
}
