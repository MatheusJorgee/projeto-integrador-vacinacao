package com.example.projetointegrador;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarActivity extends AppCompatActivity {

    LinearLayout layoutDias;
    RelativeLayout layoutTimeline;
    TextView tvTituloAgenda;
    BottomNavigationView bottomNav;

    String dataSelecionada;
    Gson gson = new Gson();
    BancoControllerAgenda banco;

    private static final int DP_POR_HORA = 120;
    private static final int HORA_INICIO = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        banco           = new BancoControllerAgenda();
        layoutDias      = findViewById(R.id.layoutDias);
        layoutTimeline  = findViewById(R.id.layoutTimeline);
        tvTituloAgenda  = findViewById(R.id.tvTituloAgenda);
        bottomNav       = findViewById(R.id.bottom_navigation);

        bottomNav.setSelectedItemId(R.id.nav_calendar);

        Calendar hoje = Calendar.getInstance();
        dataSelecionada = String.format("%04d-%02d-%02d",
                hoje.get(Calendar.YEAR),
                hoje.get(Calendar.MONTH) + 1,
                hoje.get(Calendar.DAY_OF_MONTH));

        montarFaixaDias();
        montarColunaHoras();
        atualizarAgenda();

        findViewById(R.id.btnAdicionarLembrete).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdicionarLembreteActivity.class);
            intent.putExtra("data", dataSelecionada);
            startActivityForResult(intent, 1);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, InfoUsuario.class));
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            atualizarAgenda();
        }
    }


    private void montarFaixaDias() {
        layoutDias.removeAllViews();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -3); // 3 dias antes de hoje

        String[] diasSemana = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};

        for (int i = 0; i < 7; i++) {
            String data = String.format("%04d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            int diaNum    = cal.get(Calendar.DAY_OF_MONTH);
            int diaIdx    = cal.get(Calendar.DAY_OF_WEEK) - 1;
            boolean hoje  = data.equals(dataSelecionada);

            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setPadding(dp(10), dp(6), dp(10), dp(10));

            TextView tvNome = new TextView(this);
            tvNome.setText(diasSemana[diaIdx]);
            tvNome.setTextSize(12);
            tvNome.setTextColor(hoje ? Color.parseColor("#E07090") : Color.parseColor("#999999"));
            tvNome.setGravity(Gravity.CENTER);

            TextView tvDia = new TextView(this);
            tvDia.setText(String.valueOf(diaNum));
            tvDia.setTextSize(17);
            tvDia.setTypeface(null, Typeface.BOLD);
            tvDia.setGravity(Gravity.CENTER);
            tvDia.setPadding(dp(10), dp(6), dp(10), dp(6));
            tvDia.setMinWidth(dp(38));

            if (hoje) {
                GradientDrawable circle = new GradientDrawable();
                circle.setShape(GradientDrawable.OVAL);
                circle.setColor(Color.parseColor("#E07090"));

                int size = dp(38);
                LinearLayout.LayoutParams lpCircle = new LinearLayout.LayoutParams(size, size);
                lpCircle.gravity = Gravity.CENTER_HORIZONTAL;
                tvDia.setLayoutParams(lpCircle);
                tvDia.setPadding(0, 0, 0, 0);
                tvDia.setBackground(circle);
                tvDia.setTextColor(Color.WHITE);
                tvDia.setGravity(Gravity.CENTER);
            } else {
                tvDia.setTextColor(Color.parseColor("#333333"));
            }

            item.addView(tvNome);
            item.addView(tvDia);

            if (hoje) {
                View ponto = new View(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(6), dp(6));
                lp.topMargin = dp(4);
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                ponto.setLayoutParams(lp);
                GradientDrawable dot = new GradientDrawable();
                dot.setShape(GradientDrawable.OVAL);
                dot.setColor(Color.parseColor("#E07090"));
                ponto.setBackground(dot);
                item.addView(ponto);
            }

            final String dataFinal = data;
            item.setOnClickListener(v -> {
                dataSelecionada = dataFinal;
                tvTituloAgenda.setText("Agenda - " + formatarDataExibicao(dataFinal));
                montarFaixaDias();
                atualizarAgenda();
            });

            layoutDias.addView(item);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


    private void montarColunaHoras() {
        for (int h = HORA_INICIO; h <= 18; h += 2) {
            TextView tvHora = new TextView(this);
            tvHora.setText(String.format("%02d:00", h));
            tvHora.setTextSize(12);
            tvHora.setTextColor(Color.parseColor("#AAAAAA"));

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    dp(55), RelativeLayout.LayoutParams.WRAP_CONTENT);

            lp.topMargin = dp((h - HORA_INICIO) * DP_POR_HORA);
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
            tvHora.setLayoutParams(lp);

            layoutTimeline.addView(tvHora);

            View linha = new View(this);
            linha.setBackgroundColor(Color.parseColor("#EEEEEE"));
            RelativeLayout.LayoutParams lpL = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, dp(1));
            lpL.topMargin = dp((h - HORA_INICIO) * DP_POR_HORA + 10);
            lpL.setMarginStart(dp(60));
            linha.setLayoutParams(lpL);
            layoutTimeline.addView(linha);
        }
    }


    private void atualizarAgenda() {
        tvTituloAgenda.setText("Agenda - " + formatarDataExibicao(dataSelecionada));

        // Remove apenas os cards (mantém as horas — id > 0 é card)
        for (int i = layoutTimeline.getChildCount() - 1; i >= 0; i--) {
            View v = layoutTimeline.getChildAt(i);
            if (v.getTag() != null && v.getTag().equals("card")) {
                layoutTimeline.removeViewAt(i);
            }
        }

        banco.buscarPorData(dataSelecionada, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> mostrarVazio("Sem conexão com o servidor"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    List<EventoCalendario> lista = gson.fromJson(json,
                            new TypeToken<List<EventoCalendario>>(){}.getType());
                    runOnUiThread(() -> {
                        if (lista == null || lista.isEmpty()) {
                            mostrarVazio("Nenhuma vacina agendada para este dia");
                        } else {
                            for (EventoCalendario evento : lista) {
                                adicionarCard(evento);
                            }
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> mostrarVazio("Erro ao carregar dados"));
                }
            }
        });
    }


    private void adicionarCard(EventoCalendario evento) {
        int horaEvento = extrairHora(evento);
        int alturaCard = dp(DP_POR_HORA * 2 - 16); // ocupa ~2 horas

        RelativeLayout card = new RelativeLayout(this);
        card.setTag("card");

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dp(12));
        bg.setColor(Color.parseColor("#FADADD")); // rosa claro
        card.setBackground(bg);
        card.setPadding(dp(16), dp(12), dp(16), dp(12));

        RelativeLayout.LayoutParams lpCard = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, alturaCard);

        int topOffset = dp((horaEvento - HORA_INICIO) * DP_POR_HORA + 4);
        lpCard.topMargin   = topOffset;
        lpCard.setMarginStart(dp(64)); // deixa espaço para a coluna de horas
        lpCard.bottomMargin = dp(8);
        card.setLayoutParams(lpCard);

        TextView tvVacina = new TextView(this);
        String nomeVacina = evento.getVacina() != null
                ? evento.getVacina().nomeVacina : "Vacina";
        String nomeAnimal = evento.getAnimal() != null
                ? evento.getAnimal().nome : "";
        tvVacina.setText(nomeVacina + "\npara " + nomeAnimal);
        tvVacina.setTextColor(Color.parseColor("#C06080"));
        tvVacina.setTextSize(13);
        tvVacina.setTypeface(null, Typeface.BOLD);

        RelativeLayout.LayoutParams lpTv = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTv.addRule(RelativeLayout.ALIGN_PARENT_START);
        lpTv.addRule(RelativeLayout.CENTER_VERTICAL);
        tvVacina.setLayoutParams(lpTv);


        TextView tvIcone = new TextView(this);
        String especie = evento.getAnimal() != null ? evento.getAnimal().especie : "";
        tvIcone.setText(especie.equals("Gato") ? "🐱" : "🐶");
        tvIcone.setTextSize(18);
        tvIcone.setGravity(Gravity.CENTER);

        GradientDrawable iconeBg = new GradientDrawable();
        iconeBg.setShape(GradientDrawable.OVAL);
        iconeBg.setColor(Color.WHITE);
        tvIcone.setBackground(iconeBg);

        int iconeSize = dp(36);
        RelativeLayout.LayoutParams lpIcone = new RelativeLayout.LayoutParams(iconeSize, iconeSize);
        lpIcone.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpIcone.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tvIcone.setLayoutParams(lpIcone);
        tvIcone.setPadding(dp(4), dp(4), dp(4), dp(4));

        card.addView(tvVacina);
        card.addView(tvIcone);

        layoutTimeline.addView(card);
    }


    private void mostrarVazio(String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextColor(Color.parseColor("#AAAAAA"));
        tv.setTextSize(14);
        tv.setGravity(Gravity.CENTER);
        tv.setTag("card");

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, dp(60));
        lp.topMargin   = dp(40);
        lp.setMarginStart(dp(64));
        tv.setLayoutParams(lp);

        layoutTimeline.addView(tv);
    }



    private int extrairHora(EventoCalendario evento) {
        try {
            String hora = evento.getHoraAgendada();
            if (hora == null || hora.isEmpty()) return HORA_INICIO;
            return Integer.parseInt(hora.split(":")[0]); // pega só "08" do "08:00"
        } catch (Exception e) {
            return HORA_INICIO;
        }
    }

    private String formatarDataExibicao(String data) {
        if (data == null || data.length() < 10) return data;
        String[] partes = data.split("-");
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}