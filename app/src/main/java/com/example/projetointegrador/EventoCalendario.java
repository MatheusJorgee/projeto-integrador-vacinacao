package com.example.projetointegrador;

import com.google.gson.annotations.SerializedName;

public class EventoCalendario {

    @SerializedName("id_registro")
    private int idRegistro;

    @SerializedName("data_agendada")
    private String dataAgendada;

    @SerializedName("hora_agendada")
    private String horaAgendada;

    @SerializedName("status")
    private String status;

    @SerializedName("animais")
    private AnimalInfo animal;

    @SerializedName("vacinas")
    private VacinaInfo vacina;

    public static class AnimalInfo {
        @SerializedName("nome")
        public String nome;

        @SerializedName("especie")
        public String especie;
    }

    public static class VacinaInfo {
        @SerializedName("nome_vacina")
        public String nomeVacina;
    }

    public int getIdRegistro()      { return idRegistro; }
    public String getDataAgendada() { return dataAgendada; }
    public String getHoraAgendada() { return horaAgendada; }
    public String getStatus()       { return status; }
    public AnimalInfo getAnimal()   { return animal; }
    public VacinaInfo getVacina()   { return vacina; }
}