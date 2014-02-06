package org.esupportail.catapp.admin.domain.services;

import fj.F;

import javax.ws.rs.core.Response;

import static fj.data.Option.fromString;

public abstract class AbstractService {

    public static final int OK = Response.Status.OK.getStatusCode();
    public static final int CREATED = Response.Status.CREATED.getStatusCode();

    protected static final F<String, String> toLowerCase = new F<String, String>() {
        public String f(String chaine) {
            return fromString(chaine).map(new F<String, String>() {
                public String f(String s) {
                    return s.toLowerCase();
                }
            }).orSome("");
        }
    };
}
