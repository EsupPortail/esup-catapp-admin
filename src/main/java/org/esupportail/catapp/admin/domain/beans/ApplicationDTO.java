package org.esupportail.catapp.admin.domain.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDTO implements Serializable {

    private Integer pk;
    private String code;
    private String titre;
    private String libelle;
    private String description;
    private String url;
    private String groupe;
    private Boolean visible;
    private List<String> domaines;

    private ApplicationDTO(final String code, final String titre, final String libelle, final String description,
                          final String url, final String groupe, final boolean visible, final String... domaines) {
        this.code = code;
        this.titre = titre;
        this.libelle = libelle;
        this.description = description;
        this.url = url;
        this.groupe = groupe;
        this.visible = visible;
        this.domaines = asList(domaines);
    }

    @JsonCreator
    public static ApplicationDTO applicationDTO(@JsonProperty("code") final String code,
                                                @JsonProperty("titre") final String titre,
                                                @JsonProperty("libelle") final String libelle,
                                                @JsonProperty("description") final String description,
                                                @JsonProperty("url") final String url,
                                                @JsonProperty("groupe") final String groupe,
                                                @JsonProperty("visible") final boolean visible,
                                                @JsonProperty("domaines") final String... domaines) {
        return new ApplicationDTO(code, titre, libelle, description, url, groupe, visible, domaines);
    }

    public Integer getPk() {
        return pk;
    }

    public String getCode() {
        return code;
    }

    public String getTitre() {
        return titre;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getGroupe() {
        return groupe;
    }

    public Boolean getVisible() {
        return visible;
    }

    public List<String> getDomaines() {
        return Collections.unmodifiableList(domaines);
    }

    public ApplicationDTO setPk(final Integer pk) {
        this.pk = pk;
        return this;
    }

    public ApplicationDTO setCode(final String code) {
        this.code = code;
        return this;
    }

    public ApplicationDTO setTitre(final String titre) {
        this.titre = titre;
        return this;
    }

    public ApplicationDTO setLibelle(final String libelle) {
        this.libelle = libelle;
        return this;
    }

    public ApplicationDTO setDescription(final String description) {
        this.description = description;
        return this;
    }

    public ApplicationDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public ApplicationDTO setGroupe(String groupe) {
        this.groupe = groupe;
        return this;
    }

    public ApplicationDTO setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public ApplicationDTO setDomaines(String... domaines) {
        this.domaines = asList(domaines);
        return this;
    }
}
