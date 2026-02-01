package br.edu.ifba.inf012.medHealthAPI.models.enums;

public enum CancellationReason {
  DOCTOR_CANCELED("Doutor cancelou"),
  PATIENT_CANCELED("Paciente cancelou"),
  MEDICAL_REASON("Razões medicinais"),
  PERSONAL_REASON("Razões pessoais"),
  OTHER("Outro");
  
  private String description;
  
  CancellationReason(String description){
    this.description = description;
  }

  public String getDescription(){
    return this.description;
  }
  
}