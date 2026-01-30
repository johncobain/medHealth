package br.edu.ifba.inf012.medHealthAPI.models.enums;

public enum Specialty {
    GENERAL("Geral"),
    ORTHOPEDIC("Ortopedico"),
    DENTAL("Dental"),
    PEDIATRIC("Pediatria"),
    CARDIOLOGY("Cardiologia"),
    GYNECOLOGY("Ginecologia"),
    DERMATOLOGY("Dermatologia");

    private String description;

    Specialty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
