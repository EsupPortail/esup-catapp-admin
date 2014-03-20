package org.esupportail.catapp.admin.domain.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Exists {

    private boolean exists;

    private Exists(final boolean exists) {
        this.exists = exists;
    }

    @JsonCreator
    public static Exists exists(@JsonProperty("exists") final boolean exists) {
        return new Exists(exists);
    }

    public boolean exists() {
        return exists;
    }
}
