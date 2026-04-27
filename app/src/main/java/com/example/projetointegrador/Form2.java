package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

    public class Form2 extends AppCompatActivity implements View.OnClickListener{

        Button btProximo2;
        ImageButton btVoltar2, btFechar2;
        Spinner spLocal;
        ScrollView form2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_form2);

            btProximo2 = findViewById(R.id.btProximo2);
            btVoltar2 = findViewById(R.id.btVoltar2);
            btFechar2 = findViewById(R.id.btFechar2);

            btProximo2.setOnClickListener(this);
            btVoltar2.setOnClickListener(this);
            btFechar2.setOnClickListener(this);

            spLocal = findViewById(R.id.spLocal);
            form2 = findViewById(R.id.form2);

            //A variavel cores armazena uma lista de objetos que devem ser apresentados no spinner
            String[] local = getResources().getStringArray(R.array.local);

            ArrayAdapter<String> aad = new ArrayAdapter<String>(this,
                    android.R.layout.simple_gallery_item, local);

            spLocal.setAdapter(aad);
        }

        @Override
        public void onClick(View view) {
            // se a origem do click foi no botão Tela1
            if (view.getId() == R.id.btProximo2){
                // Chamar a tela CarteiraVacinacao
                Intent form3 = new Intent(this, Form3.class);
                startActivity(form3);
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

