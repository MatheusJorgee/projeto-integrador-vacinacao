package com.example.projetointegrador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco_form.db";
    private static final int VERSAO = 6;
    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE formulario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT,"
                + "idade INTEGER,"
                + "sexo TEXT,"
                + "data TEXT,"
                + "porte TEXT,"
                + "enfermidade TEXT,"
                + "descricao TEXT,"
                + "local TEXT,"
                + "castrado TEXT,"
                + "pelo TEXT,"
                + "docil TEXT,"
                + "anotacoes TEXT"
                + ")";
        db.execSQL(sql);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS formulario");
        onCreate(db);
    }
}
