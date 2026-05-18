package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmaForms extends AppCompatActivity implements View.OnClickListener {

    Button btFinalizar;
    ImageButton btFechar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirma_forms);

        btFechar4 = findViewById(R.id.btFechar4);
        btFinalizar = findViewById(R.id.btFinalizar);

        btFechar4.setOnClickListener(this);
        btFinalizar.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btFinalizar) {

            Intent main = new Intent(this, MeusAnimais.class);
            startActivity(main);
        }
        // FECHAR E IR AO MENU
        if (view.getId() == R.id.btFechar4) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }
}