package org.esupportail.catapp.admin.domain.enums;

public enum Activation {
    Activated,
    Deactivated;

    public static Activation valueOf(boolean value) {
        return value ? Activated : Deactivated;
    }

    public static boolean isActivated(Activation activation) {
        return activation.equals(Activated);
    }

    public boolean boolValue() {
        return this.equals(Activated);
    }
}
