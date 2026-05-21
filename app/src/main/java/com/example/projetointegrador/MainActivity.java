package com.example.projetointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btCarteiraVacinacao, btMeusAnimais, btCalendarioGeral, iconUsuario, btMeuPerfil;

    BottomNavigationView bottomNav;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // BOTOES
        btCarteiraVacinacao = findViewById(R.id.btCarteiraVacinacao);
        btMeusAnimais = findViewById(R.id.btMeusAnimais);
        btCalendarioGeral = findViewById(R.id.btCalendarioGeral);
        iconUsuario = findViewById(R.id.iconUsuario);
        btMeuPerfil = findViewById(R.id.btMeuPerfil);

        btCarteiraVacinacao.setOnClickListener(this);
        btMeusAnimais.setOnClickListener(this);
        btCalendarioGeral.setOnClickListener(this);
        iconUsuario.setOnClickListener(this);
        btMeuPerfil.setOnClickListener(this);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home); // marca o ícone como ativo

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;

            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, MeusAnimais.class));
                return true;

            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, Form1.class));
                return true;

            }  else if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, InfoUsuario.class));
            return true;
        }

            return false;
        });
    }



    @Override
    public void onClick(View view) {
        // AO CLICAR EM NO BOTÃO "btCarteiraVacinacao" VAI PARA A TELA CARTEIRA E VACINACAO
        if (view.getId() == R.id.btCarteiraVacinacao){
            // Chama a tela MeusAnimais
            Intent tela1 = new Intent(this, MeusAnimais.class); // Mudar a tela!
            startActivity(tela1);
        }
        if (view.getId() == R.id.iconUsuario){
            // Chama a tela InfoUsuario
            Intent tela1 = new Intent(this, InfoUsuario.class);
            startActivity(tela1);
        }
        // AO CLICAR EM NO BOTÃO "btMeusAnimais" VAI PARA A TELA MEUS PETS/ANIMAIS
        if (view.getId() == R.id.btMeusAnimais){
            // Chama a tela MeusAnimais
            Intent pets = new Intent(this, MeusAnimais.class);
            startActivity(pets);
        }
        // AO CLICAR EM NO BOTÃO "btCalendarioGeral" VAI PARA A TELA CALENDARIO GERAL
        if (view.getId() == R.id.btCalendarioGeral){
            // Chama a tela calendar
            Intent tela2 = new Intent(this, CalendarActivity.class); // Mudar a tela!
            startActivity(tela2);
        }
        // AO CLICAR EM NO BOTÃO "btCalendarioGeral" VAI PARA A TELA MEU PERFIL
        if (view.getId() == R.id.btMeuPerfil){
            // Chamar a tela InfoUsuario
            Intent infoUsuario = new Intent(this, InfoUsuario.class);
            startActivity(infoUsuario);
        }

    }
}