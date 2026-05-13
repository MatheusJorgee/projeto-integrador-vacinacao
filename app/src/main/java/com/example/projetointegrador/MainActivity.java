package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btCarteiraVacinacao, btMeusAnimais, btCalendarioGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // BOTOES
        btCarteiraVacinacao = findViewById(R.id.btCarteiraVacinacao);
        btMeusAnimais = findViewById(R.id.btMeusAnimais);
        btCalendarioGeral = findViewById(R.id.btCalendarioGeral);

        btCarteiraVacinacao.setOnClickListener(this);
        btMeusAnimais.setOnClickListener(this);
        btCalendarioGeral.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // se a origem do click foi no botão Tela1
        if (view.getId() == R.id.btCarteiraVacinacao){
            // Chamar a tela CarteiraVacinacao
            Intent tela1 = new Intent(this, Form1.class);
            startActivity(tela1);
        }
        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btMeusAnimais){
            // Chamar a tela MeusAnimais
            Intent form1 = new Intent(this, Form1.class);
            startActivity(form1);
        }
        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btCalendarioGeral){
            // Chamar a tela MeusAnimais
            Intent tela2 = new Intent(this, Form3.class);
            startActivity(tela2);
        }

    }
}