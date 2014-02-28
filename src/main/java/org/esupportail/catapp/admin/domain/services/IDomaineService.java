package org.esupportail.catapp.admin.domain.services;


import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;


public interface IDomaineService extends IService<DomaineDTO> {

    Tree<DomaineDTO> treeDomaines() throws InterruptedException;
}
