package com.medi.flow.enumerated;

public enum ModuleType {

    SYSTEM_ADMIN("Administrador do sistema", "SYSTEM_ADMIN"),
    MED_DOCTOR("Doutor", "MED_DOCTOR"),
    MED_NURSE("Enfermeiro", "MED_NURSE"),
    MED_PATIENT("Paciente", "MED_PATIENT");

    private final String name;

    private final String prefix;

    ModuleType(final String name, final String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
}
