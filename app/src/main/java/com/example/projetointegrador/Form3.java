package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Form3 extends AppCompatActivity implements View.OnClickListener{

    Button btProximo3;
    ImageButton btVoltar3, btFechar3;
    ScrollView form3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form3);

        btProximo3 = findViewById(R.id.btProximo3);
        btVoltar3 = findViewById(R.id.btVoltar3);
        btFechar3 = findViewById(R.id.btFechar3);

        btProximo3.setOnClickListener(this);
        btVoltar3.setOnClickListener(this);
        btFechar3.setOnClickListener(this);

        form3 = findViewById(R.id.form3);

    }
    @Override
    public void onClick(View view) {
        // se a origem do click foi no botão Tela1
        if (view.getId() == R.id.btProximo3){
            // Chamar a tela CarteiraVacinacao
            Intent form3 = new Intent(this, Form3.class);
            startActivity(form3);
        }
        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btVoltar3){
            // Chamar a tela MeusAnimais
            Intent form2 = new Intent(this, Form2.class);
            startActivity(form2);
        }
        // se a origem do click foi no botão Tela2
        if (view.getId() == R.id.btFechar3){
            // Chamar a tela MeusAnimais
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }


    }
}
