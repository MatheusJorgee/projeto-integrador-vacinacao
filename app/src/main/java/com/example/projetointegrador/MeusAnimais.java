package com.example.projetointegrador;

import com.bumptech.glide.Glide;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ImageView;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class MeusAnimais extends AppCompatActivity implements View.OnClickListener {

    LinearLayout containerAnimais;
    ImageButton btCadastroPets;
    ImageButton btVoltarPets;
    BottomNavigationView bottomNav;
    List<Pet> listaCompleta = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_animais);

        // BOTOES
        btVoltarPets     = findViewById(R.id.btVoltarPets);
        btCadastroPets   = findViewById(R.id.btCadastroPets);
        containerAnimais = findViewById(R.id.containerAnimais);

        btVoltarPets.setOnClickListener(this);
        btCadastroPets.setOnClickListener(this);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                containerAnimais.removeAllViews();
                String filtro = newText.toLowerCase().trim();
                for (Pet pet : listaCompleta) {
                    if (filtro.isEmpty()
                            || pet.getNome().toLowerCase().contains(filtro)
                            || pet.getEspecie().toLowerCase().contains(filtro)) {
                        adicionarCard(pet);
                    }
                }
                return true;
            }
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_search);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, Form1.class));
            } else if (id == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, InfoUsuario.class));
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        exibirMeusAnimais();
    }

    private void exibirMeusAnimais() {
        containerAnimais.removeAllViews();
        buscarAnimaisDoSupabase();
    }

    private void buscarAnimaisDoSupabase() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/animais"
                        + "?select=id_animal,nome,especie,porte,idade,sexo,"
                        + "data_resgate,enfermidade,obs_enfermidade,"
                        + "localizacao_resgate,castrado,pelo,docil,anotacoes,fotos_animais(url_foto)")
                .get()
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MeusAnimais.this,
                                "Erro de conexão: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body() != null ? response.body().string() : "[]";
                try {
                    JsonArray array = JsonParser.parseString(json).getAsJsonArray();

                    runOnUiThread(() -> {
                        listaCompleta.clear();
                        containerAnimais.removeAllViews();

                        if (array.size() == 0) {
                            TextView tv = new TextView(MeusAnimais.this);
                            tv.setText("Nenhum animal cadastrado ainda.");
                            tv.setPadding(32, 32, 32, 32);
                            containerAnimais.addView(tv);
                            return;
                        }

                        for (JsonElement el : array) {
                            Pet pet = petFromJson(el.getAsJsonObject());
                            listaCompleta.add(pet);
                            adicionarCard(pet);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(MeusAnimais.this,
                                    "Erro ao carregar animais",
                                    Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private Pet petFromJson(JsonObject obj) {
        String nome           = str(obj, "nome");
        String especie        = str(obj, "especie");
        String sexo           = str(obj, "sexo");
        String data           = str(obj, "data_resgate");
        String porte          = str(obj, "porte");
        String enfermidade    = str(obj, "enfermidade");
        String obsEnfermidade = str(obj, "obs_enfermidade");
        String local          = str(obj, "localizacao_resgate");
        String pelo           = str(obj, "pelo");
        String docil          = str(obj, "docil");
        String anotacoes      = str(obj, "anotacoes");

        int idade = 0;
        if (obj.has("idade") && !obj.get("idade").isJsonNull()) {
            try { idade = Integer.parseInt(obj.get("idade").getAsString()); }
            catch (NumberFormatException ignored) {}
        }

        String castrado = "Não";
        if (obj.has("castrado") && !obj.get("castrado").isJsonNull()) {
            castrado = obj.get("castrado").getAsBoolean() ? "Sim" : "Não";
        }

        Pet pet = new Pet(nome, idade, sexo, data, porte, especie,
                enfermidade, obsEnfermidade, local, castrado, pelo, docil, anotacoes);

        if (obj.has("id_animal") && !obj.get("id_animal").isJsonNull()) {
            pet.setId(obj.get("id_animal").getAsInt());
        }

        if (obj.has("fotos_animais") && obj.get("fotos_animais").isJsonArray()) {
            JsonArray fotos = obj.get("fotos_animais").getAsJsonArray();
            if (fotos.size() > 0) {
                pet.setFotoUrl(fotos.get(0).getAsJsonObject().get("url_foto").getAsString());
            }
        }

        return pet;
    }

    private void adicionarCard(Pet pet) {
        View cardView = getLayoutInflater().inflate(R.layout.item_pet, containerAnimais, false);

        TextView txtNome    = cardView.findViewById(R.id.txtNomeCard);
        TextView txtEspecie = cardView.findViewById(R.id.txtEspecieCard);
        ImageView imgAnimal = cardView.findViewById(R.id.imgAnimalCard);

        txtNome.setText(pet.getNome());
        txtEspecie.setText(pet.getSexo() + ", " + pet.getIdade() + " anos");

        if (pet.getFotoUrl() != null && !pet.getFotoUrl().isEmpty()) {
            imgAnimal.clearColorFilter();
            Glide.with(this)
                    .load(pet.getFotoUrl())
                    .centerCrop()
                    .into(imgAnimal);
        } else {
            imgAnimal.setColorFilter(android.graphics.Color.parseColor("#C2185B"));
            imgAnimal.setImageResource(R.drawable.ic_animals);
        }

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(MeusAnimais.this, PerfilPetActivity.class);
            intent.putExtra("PET_OBJETO", pet);
            startActivity(intent);
        });

        containerAnimais.addView(cardView);
    }

    private String str(JsonObject obj, String campo) {
        if (obj.has(campo) && !obj.get(campo).isJsonNull()) {
            return obj.get(campo).getAsString();
        }
        return "";
    }

    @Override
    public void onClick(View view) {
        // AO CLICAR EM NO BOTÃO "btCadastroPets" VAI PARA A TELA Form1
        if (view.getId() == R.id.btCadastroPets) {
            Intent cadastrarPet = new Intent(this, Form1.class);
            startActivity(cadastrarPet);
        }
        // AO CLICAR EM NO BOTÃO "btVoltarPets" VAI PARA A TELA MainActivity
        if (view.getId() == R.id.btVoltarPets) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }
}