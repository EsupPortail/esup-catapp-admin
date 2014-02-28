package org.esupportail.catapp.admin.domain.services;

import fj.data.List;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;

public interface IService<T> {

    boolean exists(String code) throws InterruptedException;

    T getOne(String code) throws InterruptedException;

    List<T> getList() throws InterruptedException;

    void add(T dto) throws InterruptedException, CrudException;

    void update(T dto) throws InterruptedException, CrudException;

    void delete(String code) throws InterruptedException, CrudException;

}
