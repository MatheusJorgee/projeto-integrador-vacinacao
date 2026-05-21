package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.LinearLayout;
import java.util.List;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MeusAnimais extends AppCompatActivity implements View.OnClickListener{

    LinearLayout containerAnimais;
    ImageButton btCadastroPets;
    ImageButton btVoltarPets;
    BancoControllerAnimais controller;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_animais);

        // BOTOES
        btVoltarPets = findViewById(R.id.btVoltarPets);
        btCadastroPets = findViewById(R.id.btCadastroPets);
        containerAnimais = findViewById(R.id.containerAnimais);

        btVoltarPets.setOnClickListener(this);
        btCadastroPets.setOnClickListener(this);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_search);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, InfoUsuario.class));
            }
            return true;
        });

        controller = new BancoControllerAnimais(this);
        // Renderiza a lista de pets na tela
        exibirMeusAnimais();
    }
    private void exibirMeusAnimais() {
        containerAnimais.removeAllViews();
        List<Pet> listaPets = controller.listar();

        for (final Pet pet : listaPets) {
            // 1. "Infla" o layout customizado que criamos
            View cardView = getLayoutInflater().inflate(R.layout.item_pet, containerAnimais, false);

            // 2. Referencia os componentes de dentro do card
            TextView txtNome = cardView.findViewById(R.id.txtNomeCard);
            TextView txtEspecie = cardView.findViewById(R.id.txtEspecieCard);
            TextView txtId = cardView.findViewById(R.id.txtIdCard);

            // 3. POO: Define o texto usando os dados do objeto atual
            txtNome.setText(pet.getNome());
            txtEspecie.setText(pet.getEspecie() + " - " + pet.getPorte());
            txtId.setText("ID: " + pet.getId());

            // 4. Configura o clique para abrir o perfil
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MeusAnimais.this, PerfilPetActivity.class);
                    intent.putExtra("PET_OBJETO", pet);
                    startActivity(intent);
                }
            });

            // 5. Adiciona o card pronto ao container da tela
            containerAnimais.addView(cardView);
        }
    }
    @Override
    protected void onResume() {
        super.onResume(); // "onResume()" --> Toda vez que você voltar para esta tela, ele vai buscar no banco
        // e trazer os botões atualizados
        exibirMeusAnimais();
    }
        // Método utilitário para converter DP em Pixels mantendo o layout proporcional
        private int convertDpToPx(int dp) {
            float density = getResources().getDisplayMetrics().density;
            return Math.round((float) dp * density);

        }

    @Override
    public void onClick(View view) {
        // AO CLICAR EM NO BOTÃO "btCadastroPets" VAI PARA A TELA Form1
        if (view.getId() == R.id.btCadastroPets){
            // Chama a tela Form1
            Intent cadastrarPet = new Intent(this, Form1.class);
            startActivity(cadastrarPet);
        }
        // AO CLICAR EM NO BOTÃO "btVoltarPets" VAI PARA A TELA MainActivity
        if (view.getId() == R.id.btVoltarPets){
            // Chama a tela MainActivity
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }
}

