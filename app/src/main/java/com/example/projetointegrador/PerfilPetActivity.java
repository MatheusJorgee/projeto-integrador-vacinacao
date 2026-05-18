package com.example.projetointegrador;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilPetActivity extends AppCompatActivity {

    TextView tvNome, tvIdade, tvEspecie, tvPorte, tvResumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        tvNome = findViewById(R.id.tvPerfilNome);
        tvIdade = findViewById(R.id.tvPerfilIdade);
        tvEspecie = findViewById(R.id.tvPerfilEspecie);
        tvPorte = findViewById(R.id.tvPerfilPorte);
        tvResumo = findViewById(R.id.tvPerfilResumo);

        // Recupera o objeto Animal enviado pela Form3
        Pet pet = (Pet) getIntent().getSerializableExtra("PET_OBJETO");

        if (pet != null) {
            // Seta as informações na tela usando os métodos do Objeto
            tvNome.setText("Nome: " + pet.getNome());
            tvIdade.setText("Idade: " + pet.getIdade() + " anos");
            tvEspecie.setText("Espécie: " + pet.getEspecie() + " (" + pet.getSexo() + ")");
            tvPorte.setText("Porte: " + pet.getPorte());

            String resumoGeral = "Pelo: " + pet.getPelo() + "\n" +
                    "Dócil: " + pet.getDocil() + "\n" +
                    "Castrado: " + pet.getCastrado() + "\n" +
                    "Enfermidade: " + pet.getEnfermidade() + " (" + pet.getObsEnfermidade() + ")\n" +
                    "Anotações: " + pet.getAnotacoes();

            tvResumo.setText(resumoGeral);
        }
    }
}