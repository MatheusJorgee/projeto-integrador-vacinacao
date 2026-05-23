package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PerfilPetActivity extends AppCompatActivity {

    ImageView imgPerfilFoto;
    ImageButton btVoltarPerfil;
    TextView txtPerfilNome, txtPerfilSubtitulo;
    TextView chipDocil, chipPorte, chipEspecie, chipCastrado, chipPelo;
    TextView txtPerfilLocal, txtPerfilDataResgate;
    TextView txtPerfilEnfermidade, labelObsEnfermidade, txtPerfilObsEnfermidade;
    TextView txtPerfilAnotacoes;
    Button btAgendaPet, btEditarPet, btExcluirPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        imgPerfilFoto           = findViewById(R.id.imgPerfilFoto);
        btVoltarPerfil          = findViewById(R.id.btVoltarPerfil);
        txtPerfilNome           = findViewById(R.id.txtPerfilNome);
        txtPerfilSubtitulo      = findViewById(R.id.txtPerfilSubtitulo);
        chipDocil               = findViewById(R.id.chipDocil);
        chipPorte               = findViewById(R.id.chipPorte);
        chipEspecie             = findViewById(R.id.chipEspecie);
        chipCastrado            = findViewById(R.id.chipCastrado);
        chipPelo                = findViewById(R.id.chipPelo);
        txtPerfilLocal          = findViewById(R.id.txtPerfilLocal);
        txtPerfilDataResgate    = findViewById(R.id.txtPerfilDataResgate);
        txtPerfilEnfermidade    = findViewById(R.id.txtPerfilEnfermidade);
        labelObsEnfermidade     = findViewById(R.id.labelObsEnfermidade);
        txtPerfilObsEnfermidade = findViewById(R.id.txtPerfilObsEnfermidade);
        txtPerfilAnotacoes      = findViewById(R.id.txtPerfilAnotacoes);
        btAgendaPet             = findViewById(R.id.btAgendaPet);
        btEditarPet             = findViewById(R.id.btEditarPet);
        btExcluirPet            = findViewById(R.id.btExcluirPet);

        btVoltarPerfil.setOnClickListener(v -> finish());

        Pet pet = (Pet) getIntent().getSerializableExtra("PET_OBJETO");

        if (pet != null) {

            txtPerfilNome.setText(pet.getNome());
            txtPerfilSubtitulo.setText(pet.getSexo() + ", " + pet.getIdade() + " anos");

            switch (pet.getDocil()) {
                case "Sim":     chipDocil.setText("Dócil");     break;
                case "Não":     chipDocil.setText("Não Dócil"); break;
                case "Arredio": chipDocil.setText("Arrédio");   break;
                default:        chipDocil.setText(pet.getDocil()); break;
            }

            chipPorte.setText(pet.getPorte());
            chipEspecie.setText(pet.getEspecie());
            chipCastrado.setText("Sim".equals(pet.getCastrado()) ? "Castrado" : "Não castrado");

            String pelo = pet.getPelo();
            if (pelo != null && !pelo.isEmpty()) {
                chipPelo.setText("Pelo " + pelo);
            } else {
                chipPelo.setVisibility(View.GONE);
            }

            String local = pet.getLocal();
            txtPerfilLocal.setText((local == null || local.isEmpty()) ? "Não informada" : local);

            String data = pet.getData();
            txtPerfilDataResgate.setText((data == null || data.isEmpty()) ? "Não informada" : data);

            String enfermidade = pet.getEnfermidade();
            if (enfermidade == null || enfermidade.isEmpty() || "Não".equals(enfermidade)) {
                txtPerfilEnfermidade.setText("Sem enfermidades registradas");
            } else {
                txtPerfilEnfermidade.setText(enfermidade);
                String obsEnf = pet.getObsEnfermidade();
                if (obsEnf != null && !obsEnf.isEmpty()) {
                    labelObsEnfermidade.setVisibility(View.VISIBLE);
                    txtPerfilObsEnfermidade.setVisibility(View.VISIBLE);
                    txtPerfilObsEnfermidade.setText(obsEnf);
                }
            }

            String anotacoes = pet.getAnotacoes();
            txtPerfilAnotacoes.setText(
                    (anotacoes == null || anotacoes.trim().isEmpty()) ? "Sem observações" : anotacoes
            );

            if (pet.getFotoUrl() != null && !pet.getFotoUrl().isEmpty()) {
                Glide.with(this).load(pet.getFotoUrl()).centerCrop().into(imgPerfilFoto);
            } else {
                imgPerfilFoto.setScaleType(ImageView.ScaleType.CENTER);
                imgPerfilFoto.setBackgroundColor(android.graphics.Color.parseColor("#FFCDD2"));
                imgPerfilFoto.setImageResource(R.drawable.ic_animals);
                imgPerfilFoto.setColorFilter(android.graphics.Color.parseColor("#C2185B"));
            }
            btEditarPet.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilPetActivity.this, Form1.class);
                intent.putExtra("PET_OBJETO", pet);
                intent.putExtra("modoEdicao", true);
                startActivity(intent);
            });
            btExcluirPet.setOnClickListener(v -> excluirDoSupabase(pet.getId()));
        }
    }

    private void excluirDoSupabase(int id) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/animais?id_animal=eq." + id)
                .delete()
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(PerfilPetActivity.this,
                                "Erro ao excluir: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(PerfilPetActivity.this,
                                "Animal excluído com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PerfilPetActivity.this,
                                "Erro ao excluir: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}