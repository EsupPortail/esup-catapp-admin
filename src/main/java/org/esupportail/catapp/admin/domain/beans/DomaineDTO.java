package org.esupportail.catapp.admin.domain.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DomaineDTO implements Serializable {

    private String code;
    private String parent;
    private String caption;
    private List<String> domains;
    private List<String> applications;

    private DomaineDTO(final String code, final String parent, final String caption) {
        this.code = code;
        this.parent = parent;
        this.caption = caption;
        this.domains = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    @JsonCreator
    public static DomaineDTO domaineDTO(@JsonProperty("code") final String code,
                                        @JsonProperty("parent") final String parent,
                                        @JsonProperty("caption") final String caption) {
        return new DomaineDTO(code, parent, caption);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public String getCaption() {
        return caption;
    }

    public List<String> getDomains() {
        return Collections.unmodifiableList(domains);
    }

    public List<String> getApplications() {
        return Collections.unmodifiableList(applications);
    }

    public DomaineDTO setParent(final String parent) {
        this.parent = parent;
        return this;
    }

    public DomaineDTO setCaption(final String caption) {
        this.caption = caption;
        return this;
    }

    public DomaineDTO setDomains(final String... domains) {
        this.domains = asList(domains);
        return this;
    }

    public DomaineDTO setApplications(final String... applications) {
        this.applications = asList(applications);
        return this;
    }

    @Override
    public String toString() {
        return "DomaineDTO{" +
                "code='" + code + '\'' +
                ", parent='" + parent + '\'' +
                ", caption='" + caption + '\'' +
                ", domains=" + domains +
                ", applications=" + applications +
                '}';
    }
}
