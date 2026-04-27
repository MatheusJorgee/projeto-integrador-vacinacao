package com.example.projetointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Form1 extends AppCompatActivity implements View.OnClickListener {

    Button btProximo;
    ImageButton btVoltar, btFechar;
    EditText txtNome, txtIdade, txtData;
    Spinner spSexo;
    ScrollView form1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1); // R.layout busca a pasta layout
        //R.classe que é uma espécie de catalogo de tudo oq tem no projeto

        btProximo = findViewById(R.id.btProximo);
        btVoltar = findViewById(R.id.btVoltar);
        btFechar = findViewById(R.id.btFechar);

        btProximo.setOnClickListener(this);
        btVoltar.setOnClickListener(this);
        btFechar.setOnClickListener(this);

        spSexo = findViewById(R.id.spSexo);

        //CAMPOS
        txtNome = findViewById(R.id.txtNome);
        txtIdade = findViewById(R.id.txtIdade);
        txtData = findViewById(R.id.txtData);

        form1 = findViewById(R.id.form1);

        // Restaurar dados
        if(FormBanco.nome != null) {
            txtNome.setText(FormBanco.nome);
        }
        if(FormBanco.idade != 0) {
            txtIdade.setText(String.valueOf(FormBanco.idade));
        }
        if(FormBanco.dataNascimento != null) {
            txtData.setText(FormBanco.dataNascimento);
        }

        //R.id referencia todos os objetos com nome/id

        //A variavel que armazena uma lista de objetos que devem ser apresentados no spinner
        String[] sexo = getResources().getStringArray(R.array.sexo);
        ArrayAdapter<String> aad = new ArrayAdapter<String>(this,
                android.R.layout.simple_gallery_item, sexo);

        spSexo.setAdapter(aad);

        txtNome.setText(FormBanco.nome);
        txtIdade.setText(String.valueOf(FormBanco.idade));
        txtData.setText(FormBanco.dataNascimento);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btProximo) {
            String nome = txtNome.getText().toString();
            String idade = txtIdade.getText().toString();
            String data = txtData.getText().toString();
            String sexo = spSexo.getSelectedItem().toString();

            FormBanco.nome = nome;
            FormBanco.dataNascimento = data;
            FormBanco.sexo = sexo;

            if (!idade.isEmpty()) {
                FormBanco.idade = Integer.parseInt(idade);
            } else {
                FormBanco.idade = 0;
            }

            // PRÓXIMA TELA
            Intent main = new Intent(this, Form2.class);
            startActivity(main);
        }

        // VOLTAR TELA
        if (view.getId() == R.id.btVoltar) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
        // FECHAR E IR AO MENU
        if (view.getId() == R.id.btFechar) {

            FormBanco.limpar(); // limpa os dados

            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }

    }

}