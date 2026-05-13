package com.example.projetointegrador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BancoControllerAnimais {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoControllerAnimais(Context context) {
        banco = new CriaBanco(context); //cria o banco e insere os dados
    }

    public long insereDados(
            String nome, int idade, String sexo, String data, String porte,
            String enfermidade, String obsEnfermidade, String local, String castrado,
            String pelo, String docil, String anotacoes
    ) {

        ContentValues valores;
        long id;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("idade", idade);
        valores.put("sexo", sexo);
        valores.put("data", data);
        valores.put("porte", porte);
        valores.put("enfermidade", enfermidade);
        valores.put("descricao", obsEnfermidade);
        valores.put("local", local);
        valores.put("castrado", castrado);
        valores.put("pelo", pelo);
        valores.put("docil", docil);
        valores.put("anotacoes", anotacoes);


        id = db.insert("formulario", null, valores);
        db.close();

        return id;

    }

    // LISTAR REGISTROS
    public List<String> listar() {

        List<String> lista = new ArrayList<>();

        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM formulario", null);

        if (cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                int idade = cursor.getInt(cursor.getColumnIndexOrThrow("idade"));
                String sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo"));
                String porte = cursor.getString(cursor.getColumnIndexOrThrow("porte"));
                String enfermidade = cursor.getString(cursor.getColumnIndexOrThrow("enfermidade"));
                String obsEnfermidade = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String castrado = cursor.getString(cursor.getColumnIndexOrThrow("castrado"));
                String pelo = cursor.getString(cursor.getColumnIndexOrThrow("pelo"));
                String docil = cursor.getString(cursor.getColumnIndexOrThrow("docil"));
                String anotacoes = cursor.getString(cursor.getColumnIndexOrThrow("anotacoes"));

                String item = "Nome: " + nome + " | Idade: " + idade + " | Sexo: " + sexo + " | Porte: " + porte
                        + " | Enfermidade: " + enfermidade + " | obsEnfermidade: " + obsEnfermidade + " | Local: " + local
                        + " | Castrado: " + castrado  + " | Pelo: " + pelo  + " | Docil: " + docil  + " | Anotacoes: " + anotacoes ;

                lista.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lista;
    }

    public String excluirDados(int id) {
        String msg = "Registro Excluído"; //exclui

        db = banco.getWritableDatabase();

        String condicao = "id = " + id;

        int linhas;
        linhas = db.delete("formulario", condicao, null);

        if (linhas < 1) {
            msg = "Erro ao Excluir";
        }

        db.close();
        return msg;
    }
}