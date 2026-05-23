package com.example.projetointegrador;

import java.io.Serializable;

// Serializable permite passar o objeto inteiro entre Activities
public class Pet implements Serializable {
    private int id;
    private String nome;
    private int idade;
    private String sexo;
    private String data;
    private String porte;
    private String especie;
    private String enfermidade;
    private String obsEnfermidade;
    private String local;
    private String castrado;
    private String pelo;
    private String docil;
    private String anotacoes;
    private String fotoUrl;


    // Construtor
    public Pet(String nome, int idade, String sexo, String data, String porte, String especie,
               String enfermidade, String obsEnfermidade, String local, String castrado,
               String pelo, String docil, String anotacoes) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.data = data;
        this.porte = porte;
        this.especie = especie;
        this.enfermidade = enfermidade;
        this.obsEnfermidade = obsEnfermidade;
        this.local = local;
        this.castrado = castrado;
        this.pelo = pelo;
        this.docil = docil;
        this.anotacoes = anotacoes;
        this.fotoUrl = null;
    }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }


    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getSexo() {
        return sexo;
    }

    public String getData() {
        return data;
    }

    public String getPorte() {
        return porte;
    }

    public String getEspecie() {
        return especie;
    }

    public String getEnfermidade() {
        return enfermidade;
    }

    public String getObsEnfermidade() {
        return obsEnfermidade;
    }

    public String getLocal() {
        return local;
    }

    public String getCastrado() {
        return castrado;
    }

    public String getPelo() {
        return pelo;
    }

    public String getDocil() {
        return docil;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public String getFotoUrl() { return fotoUrl; }

}
