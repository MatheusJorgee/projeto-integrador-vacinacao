package com.example.projetointegrador;

public class FormBanco {
    //Form 1 - Info. básicas
    public static String nome ="";
    public static int idade = 0;
    public static String sexo = "";
    public static String dataNascimento = "";
    public static String porte = "";
    public static String especie = "";

    //From 2 - Info. Específicas
    public static String enfermidade = "";
    public static String obsEnfermidade = "";

    //Form 3 - Características
    public static String local = "";
    public static String castrado = "";
    public static String pelo = "";
    public static String docil = "";
    public static String observacao = "";

    public static void limpar() {
        nome = "";
        idade = 0;
        sexo = "";
        dataNascimento = "";
        porte = "";
        especie = "";
        enfermidade = "";
        obsEnfermidade = "";
        local = "";
        castrado = "";
        pelo = "";
        docil = "";
        observacao = "";
    }
}
