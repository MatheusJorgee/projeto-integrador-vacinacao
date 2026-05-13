package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Form1 extends AppCompatActivity implements View.OnClickListener {

    Button btProximo;
    ImageButton btVoltar, btFechar;
    EditText txtNome, txtIdade, txtData;
    RadioGroup rgPorte;
    Spinner spSexo;
    ScrollView form1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1); // R.layout busca a pasta layout
        //R.classe que é uma espécie de catalogo de tudo oq tem no projeto

        //BOTOES
        btProximo = findViewById(R.id.btProximo);
        btVoltar = findViewById(R.id.btVoltar);
        btFechar = findViewById(R.id.btFechar);

        btProximo.setOnClickListener(this);
        btVoltar.setOnClickListener(this);
        btFechar.setOnClickListener(this);

        //CAMPOS
        txtNome = findViewById(R.id.txtNome);
        txtIdade = findViewById(R.id.txtIdade);
        txtData = findViewById(R.id.txtData);
        rgPorte = findViewById(R.id.rgPorte);
        spSexo = findViewById(R.id.spSexo);

        form1 = findViewById(R.id.form1);

        //R.id referencia todos os objetos com nome/id

        // SPINNER
        String[] sexo = getResources().getStringArray(R.array.sexo);
        ArrayAdapter<String> aad = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sexo);

        spSexo.setAdapter(aad);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btProximo) {

            String nome = txtNome.getText().toString();
            String idadeStr = txtIdade.getText().toString();
            String data = txtData.getText().toString();
            String sexo = spSexo.getSelectedItem().toString();

            int idade = 0;
            if (!idadeStr.isEmpty()) {
                idade = Integer.parseInt(idadeStr);
            }

            // CAPTURAR PORTE
            int idSelecionado = rgPorte.getCheckedRadioButtonId();

            if (idSelecionado == -1) {
                Toast.makeText(this, "Selecione o porte!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton radioSelecionado = findViewById(idSelecionado);
            String porte = radioSelecionado.getText().toString();

            Intent intent = new Intent(this, Form2.class);

            intent.putExtra("nome", nome);
            intent.putExtra("idade", idade);
            intent.putExtra("sexo", sexo);
            intent.putExtra("data", data);
            intent.putExtra("porte", porte);

            // Ir para próxima tela
            startActivity(intent);
        }

        // VOLTAR TELA
        if (view.getId() == R.id.btVoltar) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
        // FECHAR E IR AO MENU
        if (view.getId() == R.id.btFechar) {

         //  FormBanco.limpar(); // limpa os dados

            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }

    }

}