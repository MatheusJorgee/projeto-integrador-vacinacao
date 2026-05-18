package com.example.projetointegrador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoControllerUsuarios {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoControllerUsuarios(Context context) {
        banco = new CriaBanco(context); //cria o banco e insere os dados
    }

    public Cursor ConsultaLogin(String email, String senha) {
        Cursor cursor;
        String[] campos = { "codigo", "nome", "email", "cpf", "senha", "telefone" };
        String where = "email='" + email + "' and senha ='" + senha + "'";
        db = banco.getReadableDatabase();
        cursor = db.query("usuarios", campos, where, null, null, null,
                null, null); //consulta
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

}

