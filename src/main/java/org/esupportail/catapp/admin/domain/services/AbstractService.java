package org.esupportail.catapp.admin.domain.services;

import fj.F;

import static fj.data.Option.fromString;
import static javax.ws.rs.core.Response.Status;

public abstract class AbstractService {

    public static final int OK = Status.OK.getStatusCode();
    public static final int CREATED = Status.CREATED.getStatusCode();
    public static final int NO_CONTENT = Status.NO_CONTENT.getStatusCode();

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
