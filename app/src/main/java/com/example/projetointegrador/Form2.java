package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Form2 extends AppCompatActivity implements View.OnClickListener{

    EditText txtEnfermidade;
    Button btProximo2;
    ImageButton btVoltar2, btFechar2;
    RadioGroup rgCastrado, rgEnfermidade;
    Spinner spLocal;
    ScrollView form2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form2);

        //BOTOES
        btProximo2 = findViewById(R.id.btProximo2);
        btVoltar2 = findViewById(R.id.btVoltar2);
        btFechar2 = findViewById(R.id.btFechar2);

        btProximo2.setOnClickListener(this);
        btVoltar2.setOnClickListener(this);
        btFechar2.setOnClickListener(this);

        //CAMPOS
        rgEnfermidade = findViewById(R.id.rgEnfermidade);
        txtEnfermidade = findViewById(R.id.txtEnfermidade);
        rgCastrado = findViewById(R.id.rgCastrado);
        spLocal = findViewById(R.id.spLocal);

        form2 = findViewById(R.id.form2);

        // SPINNER LOCAL
        String[] local = getResources().getStringArray(R.array.local);
        ArrayAdapter<String> aad = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, local);

        spLocal.setAdapter(aad);
    }

    @Override
    public void onClick(View view) {
        // AÇOES AO CLICAR NO BOTAO "PROXIMO" DO FORM2
        if (view.getId() == R.id.btProximo2){
            String obsEnfermidade = txtEnfermidade.getText().toString();
            String local = spLocal.getSelectedItem().toString();

            // RADIO ENFERMIDADE
            int idEnfermidade = rgEnfermidade.getCheckedRadioButtonId();

            if (idEnfermidade == -1) {
                Toast.makeText(this, "Selecione a enfermidade!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rbEnfermidade = findViewById(idEnfermidade);
            String enfermidade = rbEnfermidade.getText().toString();

            // RADIO CASTRADO
            int idCastrado = rgCastrado.getCheckedRadioButtonId();

            if (idCastrado == -1) {
                Toast.makeText(this, "Selecione se é castrado!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rbCastrado = findViewById(idCastrado);
            String castrado = rbCastrado.getText().toString();

            //RECEBE DADOS DO FORM1
            Intent intent = getIntent();

            String nome = intent.getStringExtra("nome");
            int idade = intent.getIntExtra("idade", 0);
            String sexo = intent.getStringExtra("sexo");
            String data = intent.getStringExtra("data");
            String porte = intent.getStringExtra("porte");

            Intent form3 = new Intent(this, Form3.class);

            //ENVIA DADOS DO FORM1 e 2 PARA O FORM3
                form3.putExtra("nome", nome);
                form3.putExtra("idade", idade);
                form3.putExtra("sexo", sexo);
                form3.putExtra("data", data);
                form3.putExtra("porte", porte);
                //
                form3.putExtra("enfermidade", enfermidade);
                form3.putExtra("obsEnfermidade", obsEnfermidade);
                form3.putExtra("local", local);
                form3.putExtra("castrado", castrado);

            startActivity(form3);

            //  SALVAR NO BANCO
           // BancoControllerAnimais controller = new BancoControllerAnimais(this);
            //controller.insereDados(nome, idade, sexo, data, porte, enfermidade, obsEnfermidade, local, castrado, null, null, null);

            //Toast.makeText(this, "Dados salvos!", Toast.LENGTH_SHORT).show();

            // Ir para próxima tela
        }

        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btVoltar2){
            // Chamar a tela MeusAnimais
            Intent form1 = new Intent(this, Form1.class);
            startActivity(form1);
        }
        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btFechar2){
            // Chamar a tela MeusAnimais
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }


    }
}

