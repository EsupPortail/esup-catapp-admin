package org.esupportail.catapp.admin.web.pojo;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ApplicationPojo {

    @Size(min = 1, max = 20)
    private String code = "";

    @Size(min = 1, max = 200)
    private String titre = "";

    @Size(max = 200)
    private String libelle = "";

    @Size(max = 3000)
    private String description = "";

    @Size(min = 1, max = 1000)
    private String url = "";

    @Size(min = 1, max = 200)
    private String groupe = "";
    
    private boolean accessibilite = true;

    private List<String> domaines = new ArrayList<>();

    public ApplicationPojo(ApplicationPojo pojo) {
        this.code = pojo.getCode();
        this.titre = pojo.getTitre();
        this.libelle = pojo.getLibelle();
        this.description = pojo.getDescription();
        this.url = pojo.getUrl();
        this.groupe = pojo.getGroupe();
        this.accessibilite = pojo.isAccessibilite();
        this.domaines = pojo.getDomaines();
    }

    public ApplicationPojo() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public ApplicationPojo withCode(final String code) {
        setCode(code);
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(final String titre) {
        this.titre = titre;
    }

    public ApplicationPojo withTitre(final String titre) {
        setTitre(titre);
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(final String libelle) {
        this.libelle = libelle;
    }

    public ApplicationPojo withLibelle(final String libelle) {
        setLibelle(libelle);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ApplicationPojo withDescription(final String description) {
       setDescription(description);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public ApplicationPojo withtUrl(final String url) {
        setUrl(url);
        return this;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(final String groupe) {
        this.groupe = groupe;
    }

    public ApplicationPojo withGroupe(final String groupe) {
        setGroupe(groupe);
        return this;
    }

    public boolean isAccessibilite() {
        return accessibilite;
    }

    public void setAccessibilite(final boolean accessibilite) {
        this.accessibilite = accessibilite;
    }

    public ApplicationPojo withAccessibilite(final boolean accessibilite) {
        setAccessibilite(accessibilite);
        return this;
    }

    public List<String> getDomaines() {
        return domaines;
    }

    public void setDomaines(final List<String> domaines) {
        this.domaines = domaines;
    }

    public ApplicationPojo withDomaines(final List<String> domaines) {
        setDomaines(domaines);
        return this;
    }

    public String getItemLabel() {
        return format("%s %s", getTitre(), getLibelle()).trim();
    }
}
