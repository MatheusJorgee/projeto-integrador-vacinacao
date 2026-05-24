package com.example.projetointegrador;

import android.annotation.SuppressLint;
import com.google.gson.JsonObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Form3 extends AppCompatActivity implements View.OnClickListener {

    Button btProximo3;
    ImageButton btVoltar3, btFechar3;
    EditText txtPelo, txtAnotacoes;
    RadioGroup rgDocil;
    ScrollView form3;
    Button btAnexar;
    ImageView imgPreview;
    String fotoUrl = null;
    Uri selectedImageUri = null;
    ActivityResultLauncher<Intent> imagePickerLauncher;

    Pet petEdicao = null;
    boolean modoEdicao = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form3);

        btProximo3 = findViewById(R.id.btProximo3);
        btVoltar3  = findViewById(R.id.btVoltar3);
        btFechar3  = findViewById(R.id.btFechar3);

        btProximo3.setOnClickListener(this);
        btVoltar3.setOnClickListener(this);
        btFechar3.setOnClickListener(this);

        txtPelo      = findViewById(R.id.txtPelo);
        rgDocil      = findViewById(R.id.rgDocil);
        txtAnotacoes = findViewById(R.id.txtAnotacoes);
        form3        = findViewById(R.id.form3);
        btAnexar     = findViewById(R.id.button2);
        imgPreview   = findViewById(R.id.imgPreviewFoto);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imgPreview.setImageURI(selectedImageUri);
                        imgPreview.setVisibility(View.VISIBLE);
                    }
                }
        );

        btAnexar.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            imagePickerLauncher.launch(pick);
        });

        modoEdicao = getIntent().getBooleanExtra("modoEdicao", false);
        if (modoEdicao) {
            petEdicao = (Pet) getIntent().getSerializableExtra("PET_OBJETO");
            preencherCampos();
            btProximo3.setText("Salvar alterações");
        }
    }

    private void preencherCampos() {
        if (petEdicao == null) return;
        txtPelo.setText(petEdicao.getPelo());
        txtAnotacoes.setText(petEdicao.getAnotacoes());

        switch (petEdicao.getDocil()) {
            case "Sim": rgDocil.check(R.id.rbSimDocil); break;
            case "Não": rgDocil.check(R.id.rbNaoDocil); break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btProximo3) {

            String pelo      = txtPelo.getText().toString();
            String anotacoes = txtAnotacoes.getText().toString();

            int idDocil = rgDocil.getCheckedRadioButtonId();
            if (idDocil == -1) {
                Toast.makeText(this, "Selecione se é dócil!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rbDocil = findViewById(idDocil);
            String docil = rbDocil.getText().toString();

            Intent intent        = getIntent();
            String nome          = intent.getStringExtra("nome");
            int    idade         = intent.getIntExtra("idade", 0);
            String sexo          = intent.getStringExtra("sexo");
            String data          = intent.getStringExtra("data");
            String porte         = intent.getStringExtra("porte");
            String especie       = intent.getStringExtra("especie");
            String enfermidade   = intent.getStringExtra("enfermidade");
            String obsEnfermidade = intent.getStringExtra("obsEnfermidade");
            String local         = intent.getStringExtra("local");
            String castrado      = intent.getStringExtra("castrado");

            JsonObject json = new JsonObject();
            json.addProperty("nome",               nome);
            json.addProperty("especie",            especie);
            json.addProperty("idade",              String.valueOf(idade));
            json.addProperty("sexo",               sexo);
            json.addProperty("porte",              porte);
            json.addProperty("enfermidade",        enfermidade);
            json.addProperty("obs_enfermidade",    obsEnfermidade);
            json.addProperty("localizacao_resgate", local);
            json.addProperty("castrado",           "Sim".equals(castrado));
            json.addProperty("pelo",               pelo);
            json.addProperty("docil",              docil);
            json.addProperty("anotacoes",          anotacoes);
            if (data != null && !data.isEmpty()) {
                json.addProperty("data_resgate", data);
            }

            if (modoEdicao && petEdicao != null) {
                if (selectedImageUri != null) {
                    uploadImagemEAtualizar(json, petEdicao.getId());
                } else {
                    atualizarNoSupabase(json.toString(), petEdicao.getId());
                }
            } else {
                if (selectedImageUri != null) {
                    uploadImagemESalvar(json);
                } else {
                    salvarNoSupabase(json.toString());
                }
            }
        }

        if (view.getId() == R.id.btVoltar3) {
            startActivity(new Intent(this, Form2.class));
        }

        if (view.getId() == R.id.btFechar3) {
            startActivity(new Intent(this, MeusAnimais.class));
        }
    }

    private void atualizarNoSupabase(String jsonBody, int id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/animais?id_animal=eq." + id)
                .patch(body)
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .addHeader("Content-Type",  "application/json")
                .addHeader("Prefer",        "return=minimal")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(Form3.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(Form3.this, "Animal atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Form3.this, MeusAnimais.class));
                        finish();
                    });
                } else {
                    String erro = response.body() != null ? response.body().string() : "erro";
                    runOnUiThread(() ->
                            Toast.makeText(Form3.this, "Erro ao atualizar: " + erro, Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }

    private void uploadImagemEAtualizar(JsonObject json, int id) {
        try {
            InputStream is = getContentResolver().openInputStream(selectedImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = is.read(buffer)) != -1) baos.write(buffer, 0, n);
            byte[] imageBytes = baos.toByteArray();

            String filename = "pet_" + System.currentTimeMillis() + ".jpg";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

            Request request = new Request.Builder()
                    .url(SupabaseConfig.URL + "/storage/v1/object/animais-fotos/" + filename)
                    .post(body)
                    .addHeader("apikey",        SupabaseConfig.API_KEY)
                    .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                    .addHeader("Content-Type",  "image/jpeg")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(Form3.this,
                            "Erro ao enviar foto: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        fotoUrl = SupabaseConfig.URL + "/storage/v1/object/public/animais-fotos/" + filename;
                        deletarFotosAntigas(id, fotoUrl);
                        runOnUiThread(() -> atualizarNoSupabase(json.toString(), id));
                    } else {
                        String erro = response.body() != null ? response.body().string() : "erro";
                        runOnUiThread(() -> Toast.makeText(Form3.this,
                                "Erro upload: " + erro, Toast.LENGTH_LONG).show());
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao ler imagem: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deletarFotosAntigas(int idAnimal, String novaUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/fotos_animais?id_animal=eq." + idAnimal)
                .delete()
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                inserirFoto(idAnimal, novaUrl);
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                inserirFoto(idAnimal, novaUrl);
            }
        });
    }

    private void uploadImagemESalvar(JsonObject json) {
        try {
            InputStream is = getContentResolver().openInputStream(selectedImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = is.read(buffer)) != -1) baos.write(buffer, 0, n);
            byte[] imageBytes = baos.toByteArray();

            String filename = "pet_" + System.currentTimeMillis() + ".jpg";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));
            Request request = new Request.Builder()
                    .url(SupabaseConfig.URL + "/storage/v1/object/animais-fotos/" + filename)
                    .post(body)
                    .addHeader("apikey",        SupabaseConfig.API_KEY)
                    .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                    .addHeader("Content-Type",  "image/jpeg")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(Form3.this,
                            "Erro ao enviar foto: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        fotoUrl = SupabaseConfig.URL + "/storage/v1/object/public/animais-fotos/" + filename;
                        runOnUiThread(() -> salvarNoSupabase(json.toString()));
                    } else {
                        String erro = response.body() != null ? response.body().string() : "erro";
                        runOnUiThread(() -> Toast.makeText(Form3.this,
                                "Erro upload: " + erro, Toast.LENGTH_LONG).show());
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao ler imagem: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void inserirFoto(int idAnimal, String url) {
        OkHttpClient client = new OkHttpClient();
        JsonObject json = new JsonObject();
        json.addProperty("id_animal", idAnimal);
        json.addProperty("url_foto",  url);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/fotos_animais")
                .post(body)
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .addHeader("Content-Type",  "application/json")
                .addHeader("Prefer",        "return=minimal")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(Form3.this, "Animal salvo, mas erro na foto.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Form3.this, MeusAnimais.class));
                    finish();
                });
            }
            @Override public void onResponse(Call call, Response response) {
                runOnUiThread(() -> {
                    Toast.makeText(Form3.this, "Animal cadastrado!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Form3.this, MeusAnimais.class));
                    finish();
                });
            }
        });
    }

    private void salvarNoSupabase(String jsonBody) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(SupabaseConfig.URL + "/rest/v1/animais")
                .post(body)
                .addHeader("apikey",        SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .addHeader("Content-Type",  "application/json")
                .addHeader("Prefer",        "return=representation")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(Form3.this, "Erro de conexão: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "[]";
                    int idAnimal = -1;
                    try {
                        JsonArray arr = JsonParser.parseString(respBody).getAsJsonArray();
                        if (arr.size() > 0)
                            idAnimal = arr.get(0).getAsJsonObject().get("id_animal").getAsInt();
                    } catch (Exception ignored) {}

                    final int finalId = idAnimal;
                    if (fotoUrl != null && finalId != -1) {
                        inserirFoto(finalId, fotoUrl);
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(Form3.this, "Animal cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Form3.this, MeusAnimais.class));
                            finish();
                        });
                    }
                } else {
                    String erro = response.body() != null ? response.body().string() : "erro desconhecido";
                    runOnUiThread(() ->
                            Toast.makeText(Form3.this, "Erro ao salvar: " + erro, Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}