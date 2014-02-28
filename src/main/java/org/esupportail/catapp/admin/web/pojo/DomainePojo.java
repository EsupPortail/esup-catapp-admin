package org.esupportail.catapp.admin.web.pojo;

import fj.F;
import fj.data.Option;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static fj.data.Array.iterableArray;
import static fj.data.Option.*;

public class DomainePojo {

    @Size(min = 1, max = 20)
    private String code = "";
    
    private Option<String> parent = none();

    @Size(min = 2, max = 200)
    private String libelle = "";
    
    private List<DomainePojo> domaines = new ArrayList<>();
    
    private List<String> applications = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public DomainePojo withCode(final String code) {
        setCode(code);
        return this;
    }

    public String getParent() {
        return parent.toNull();
    }

    public DomainePojo withParent(final String parent) {
        setParent(parent);
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public DomainePojo withLibelle(final String libelle) {
        setLibelle(libelle);
        return this;
    }

    public List<DomainePojo> getDomaines() {
        return domaines;
    }

    public DomainePojo withDomaines(List<DomainePojo> domaines) {
        setDomaines(domaines);
        return this;
    }

    public List<String> getApplications() {
        return applications;
    }

    public DomainePojo withApplications(List<String> applications) {
        setApplications(applications);
        return this;
    }

    public boolean isDeletable() {
        final boolean noApps = fromNull(applications).map(new F<List<String>, Boolean>() {
            public Boolean f(List<String> apps) {
                return apps.isEmpty();
            }

            ;
        }).orSome(false);

        final boolean noDomains = fromNull(domaines).map(new F<List<DomainePojo>, Boolean>() {
            public Boolean f(List<DomainePojo> doms) {
                return doms.isEmpty();
            }

            ;
        }).orSome(false);

        final boolean subDomainsDeletable = fromNull(domaines).map(new F<List<DomainePojo>, Boolean>() {
            public Boolean f(List<DomainePojo> domainePojos) {
                return iterableArray(domainePojos).forall(new F<DomainePojo, Boolean>() {
                    public Boolean f(DomainePojo pojo) {
                        return pojo.isDeletable();
                    }
                });
            }
        }).orSome(false);

        return parent.isSome() && noApps && noDomains && subDomainsDeletable;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setParent(final String parent) {
        this.parent = fromString(parent);
    }

    public void setLibelle(final String libelle) {
        this.libelle = libelle;
    }

    public void setDomaines(final List<DomainePojo> domaines) {
        this.domaines = domaines;
    }

    public void setApplications(final List<String> applications) {
        this.applications = applications;
    }
}
