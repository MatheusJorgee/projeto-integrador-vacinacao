package com.example.projetointegrador;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BancoControllerAgenda {

    private final OkHttpClient client = new OkHttpClient();

    public void buscarPorData(String data, Callback callback) {
        String url = SupabaseConfig.URL
                + "/rest/v1/carteira_vacinacao"
                + "?select=id_registro,data_agendada,hora_agendada,status,"
                + "animais(nome,especie),"
                + "vacinas(nome_vacina)"
                + "&data_agendada=eq." + data
                + "&order=data_agendada.asc";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("apikey", SupabaseConfig.API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }
}