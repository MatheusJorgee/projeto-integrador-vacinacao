package com.example.projetointegrador;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.DatePickerDialog;


    public class AdicionarLembreteActivity extends AppCompatActivity {
        Spinner spinnerAnimal, spinnerVacina;
        TextView tvData, tvHoraInicio, tvHoraFim;
        List<String> nomesAnimais = new ArrayList<>();
        List<Integer> idsAnimais  = new ArrayList<>();
        List<String> nomesVacinas = new ArrayList<>();
        List<Integer> idsVacinas  = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_adicionar_lembrete);

            spinnerAnimal = findViewById(R.id.spinnerAnimal);
            spinnerVacina = findViewById(R.id.spinnerVacina);
            tvData        = findViewById(R.id.tvDataEscolhida);
            tvHoraInicio  = findViewById(R.id.tvHoraInicio);
            tvHoraFim     = findViewById(R.id.tvHoraFim);

            String data = getIntent().getStringExtra("data");
            tvData.setText(data);

            tvData.setOnClickListener(v -> {
                String[] partes = tvData.getText().toString().split("-");
                int ano = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]) - 1;
                int dia = Integer.parseInt(partes[2]);

                DatePickerDialog picker = new DatePickerDialog(this,
                        (view, y, m, d) -> {
                            String dataSelecionada = String.format("%04d-%02d-%02d", y, m + 1, d);
                            tvData.setText(dataSelecionada);
                        }, ano, mes, dia);

                picker.getDatePicker().setMinDate(System.currentTimeMillis());

                picker.show();
            });

            carregarAnimais();
            carregarVacinas();

            tvHoraInicio.setOnClickListener(v ->
                    new android.app.TimePickerDialog(this, (view, h, m) ->
                            tvHoraInicio.setText(String.format("%02d:%02d", h, m)),
                            8, 0, true).show());

            tvHoraFim.setOnClickListener(v ->
                    new android.app.TimePickerDialog(this, (view, h, m) ->
                            tvHoraFim.setText(String.format("%02d:%02d", h, m)),
                            10, 0, true).show());

            findViewById(R.id.btnSalvar).setOnClickListener(v -> salvar());
            findViewById(R.id.btnFechar).setOnClickListener(v -> finish());
        }

        private void carregarAnimais() {
            String url = SupabaseConfig.URL + "/rest/v1/animais?select=id_animal,nome";
            Request req = new Request.Builder().url(url).get()
                    .addHeader("apikey", SupabaseConfig.API_KEY)
                    .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                    .build();
            new OkHttpClient().newCall(req).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {}
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JsonArray arr = JsonParser.parseString(response.body().string()).getAsJsonArray();
                    for (JsonElement el : arr) {
                        JsonObject obj = el.getAsJsonObject();
                        idsAnimais.add(obj.get("id_animal").getAsInt());
                        nomesAnimais.add(obj.get("nome").getAsString());
                    }
                    runOnUiThread(() -> spinnerAnimal.setAdapter(
                            new ArrayAdapter<>(AdicionarLembreteActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, nomesAnimais)));
                }
            });
        }

        private void carregarVacinas() {
            String url = SupabaseConfig.URL + "/rest/v1/vacinas?select=id_vacina,nome_vacina";
            Request req = new Request.Builder().url(url).get()
                    .addHeader("apikey", SupabaseConfig.API_KEY)
                    .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                    .build();
            new OkHttpClient().newCall(req).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {}
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JsonArray arr = JsonParser.parseString(response.body().string()).getAsJsonArray();
                    for (JsonElement el : arr) {
                        JsonObject obj = el.getAsJsonObject();
                        idsVacinas.add(obj.get("id_vacina").getAsInt());
                        nomesVacinas.add(obj.get("nome_vacina").getAsString());
                    }
                    runOnUiThread(() -> spinnerVacina.setAdapter(
                            new ArrayAdapter<>(AdicionarLembreteActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, nomesVacinas)));
                }
            });
        }

        private void salvar() {
            if (idsAnimais.isEmpty() || idsVacinas.isEmpty()) {
                Toast.makeText(this, "Carregando dados...", Toast.LENGTH_SHORT).show();
                return;
            }
            int idAnimal = idsAnimais.get(spinnerAnimal.getSelectedItemPosition());
            int idVacina = idsVacinas.get(spinnerVacina.getSelectedItemPosition());
            String data  = tvData.getText().toString();

            String json = "{\"id_animal\":" + idAnimal
                    + ",\"id_vacina\":" + idVacina
                    + ",\"data_agendada\":\"" + data + "\""
                    + ",\"status\":\"Pendente\"}";

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request req = new Request.Builder()
                    .url(SupabaseConfig.URL + "/rest/v1/carteira_vacinacao")
                    .post(body)
                    .addHeader("apikey", SupabaseConfig.API_KEY)
                    .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();

            new OkHttpClient().newCall(req).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(AdicionarLembreteActivity.this,
                            "Erro de conexão", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(Call call, Response response) {
                    runOnUiThread(() -> {
                        Toast.makeText(AdicionarLembreteActivity.this,
                                "Lembrete salvo!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                }
            });
        }
    }
