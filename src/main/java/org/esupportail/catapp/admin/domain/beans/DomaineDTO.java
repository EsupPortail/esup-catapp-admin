package org.esupportail.catapp.admin.domain.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fj.data.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fj.data.Option.fromString;
import static java.util.Arrays.asList;

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomaineDTO implements Serializable {

    private Integer pk;
    private String code;
    private Option<String> parent;
    private String libelle;
    private List<String> domaines;
    private List<String> applications;

    private DomaineDTO(final String code, final String parent, final String libelle) {
        this.code = code;
        this.parent = fromString(parent);
        this.libelle = libelle;
        this.domaines = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    @JsonCreator
    public static DomaineDTO domaineDTO(@JsonProperty("code") final String code,
                                        @JsonProperty("parent") final String parent,
                                        @JsonProperty("libelle") final String libelle) {
        return new DomaineDTO(code, parent, libelle);
    }

    public Integer getPk() {
        return pk;
    }

    public String getCode() {
        return code;
    }

    public Option<String> getParent() {
        return parent;
    }

    public String getLibelle() {
        return libelle;
    }

    public List<String> getDomaines() {
        return Collections.unmodifiableList(domaines);
    }

    public List<String> getApplications() {
        return Collections.unmodifiableList(applications);
    }

    public DomaineDTO setPk(final Integer pk) {
        this.pk = pk;
        return this;
    }

    public DomaineDTO setCode(final String code) {
        this.code = code;
        return this;
    }

    public DomaineDTO setParent(final String parent) {
        this.parent = fromString(parent);
        return this;
    }

    public DomaineDTO setLibelle(final String libelle) {
        this.libelle = libelle;
        return this;
    }

    public DomaineDTO setDomaines(final String... domaines) {
        this.domaines = asList(domaines);
        return this;
    }

    public DomaineDTO setApplications(final String... applications) {
        this.applications = asList(applications);
        return this;
    }
}
