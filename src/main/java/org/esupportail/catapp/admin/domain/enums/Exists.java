package org.esupportail.catapp.admin.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Exists {
    ;
    private final boolean value;

    @JsonCreator
    Exists(@JsonProperty("exists") final boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }
}
