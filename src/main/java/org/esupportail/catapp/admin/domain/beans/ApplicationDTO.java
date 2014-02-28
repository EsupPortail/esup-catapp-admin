package org.esupportail.catapp.admin.domain.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.esupportail.catapp.admin.domain.enums.Activation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDTO implements Serializable {

    private String code;
    private String title;
    private String caption;
    private String description;
    private String url;
    private String group;
    private Activation activation;
    private List<String> domains;

    private ApplicationDTO(final String code, final String title, final String caption, final String description,
                          final String url, final String group, final Activation activation, final String... domains) {
        this.code = code;
        this.title = title;
        this.caption = caption;
        this.description = description;
        this.url = url;
        this.group = group;
        this.activation = activation;
        this.domains = asList(domains);
    }



    @JsonCreator
    public static ApplicationDTO applicationDTO(@JsonProperty("code") final String code,
                                                @JsonProperty("title") final String title,
                                                @JsonProperty("caption") final String caption,
                                                @JsonProperty("description") final String description,
                                                @JsonProperty("url") final String url,
                                                @JsonProperty("group") final String group,
                                                @JsonProperty("activation") final Activation activation,
                                                @JsonProperty("domains") final String... domains) {
        return new ApplicationDTO(code, title, caption, description, url, group, activation, domains);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public ApplicationDTO setTitle(final String title) {
        this.title = title;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public ApplicationDTO setCaption(final String caption) {
        this.caption = caption;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ApplicationDTO setDescription(final String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ApplicationDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public ApplicationDTO setGroup(String group) {
        this.group = group;
        return this;
    }

    public Activation getActivation() {
        return activation;
    }

    public ApplicationDTO setActivation(Activation activation) {
        this.activation = activation;
        return this;
    }

    public List<String> getDomains() {
        return Collections.unmodifiableList(domains);
    }

    public ApplicationDTO setDomains(String... domains) {
        this.domains = asList(domains);
        return this;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", group='" + group + '\'' +
                ", activation=" + activation +
                ", domains=" + domains +
                '}';
    }
}
