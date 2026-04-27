package com.example.projetointegrador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco  extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco_cadastro.db";
    private static final int VERSAO = 1;
    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cadastro ("
                + "codigo integer primary key autoincrement,"
                + "nome text,"
                + "idade int,"
                + "sexo text,"
                + "dtNasicmento text,"
                + "porte text,"
                + "especie text,"
                + "enfermidade text,"
                + "obsEnfermidade text,"
                + "local text,"
                + "castrado text,"
                + "pelo text,"
                + "docil text,"
                + "observacao text,"
                + "arquivo text)";
        db.execSQL(sql);

        sql = "insert into usuarios (nome, email, senha, cpf, telefone)" +
                "values ('ADMIN', 'admin@teste.com', 'adm123', null, null)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cadastro");
        onCreate(db);
    }
}
