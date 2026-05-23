package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Form2 extends AppCompatActivity implements View.OnClickListener {

    EditText txtEnfermidade;
    Button btProximo2;
    ImageButton btVoltar2, btFechar2;
    RadioGroup rgCastrado, rgEnfermidade;
    Spinner spLocal;
    ScrollView form2;

    Pet petEdicao = null;
    boolean modoEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form2);

        btProximo2 = findViewById(R.id.btProximo2);
        btVoltar2  = findViewById(R.id.btVoltar2);
        btFechar2  = findViewById(R.id.btFechar2);

        btProximo2.setOnClickListener(this);
        btVoltar2.setOnClickListener(this);
        btFechar2.setOnClickListener(this);

        rgEnfermidade  = findViewById(R.id.rgEnfermidade);
        txtEnfermidade = findViewById(R.id.txtEnfermidade);
        rgCastrado     = findViewById(R.id.rgCastrado);
        spLocal        = findViewById(R.id.spLocal);
        form2          = findViewById(R.id.form2);

        String[] local = getResources().getStringArray(R.array.local);
        spLocal.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, local));

        modoEdicao = getIntent().getBooleanExtra("modoEdicao", false);
        if (modoEdicao) {
            petEdicao = (Pet) getIntent().getSerializableExtra("PET_OBJETO");
            preencherCampos(local);
            btProximo2.setText("Próximo (Edição)");
        }
    }

    private void preencherCampos(String[] localArr) {
        if (petEdicao == null) return;

        txtEnfermidade.setText(petEdicao.getObsEnfermidade());

        switch (petEdicao.getEnfermidade()) {
            case "Sim": rgEnfermidade.check(R.id.rbSim); break;
            case "Não": rgEnfermidade.check(R.id.rbNao); break;
        }

        switch (petEdicao.getCastrado()) {
            case "Sim": rgCastrado.check(R.id.rbSimCastrado); break;
            case "Não": rgCastrado.check(R.id.rbNaoCastrado); break;
        }

        for (int i = 0; i < localArr.length; i++) {
            if (localArr[i].equals(petEdicao.getLocal())) {
                spLocal.setSelection(i); break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btProximo2) {

            String obsEnfermidade = txtEnfermidade.getText().toString();
            String local          = spLocal.getSelectedItem().toString();

            int idEnfermidade = rgEnfermidade.getCheckedRadioButtonId();
            if (idEnfermidade == -1) {
                Toast.makeText(this, "Selecione a enfermidade!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rbEnfermidade = findViewById(idEnfermidade);
            String enfermidade = rbEnfermidade.getText().toString();

            int idCastrado = rgCastrado.getCheckedRadioButtonId();
            if (idCastrado == -1) {
                Toast.makeText(this, "Selecione se é castrado!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rbCastrado = findViewById(idCastrado);
            String castrado = rbCastrado.getText().toString();

            Intent intent   = getIntent();
            String nome     = intent.getStringExtra("nome");
            int    idade    = intent.getIntExtra("idade", 0);
            String sexo     = intent.getStringExtra("sexo");
            String data     = intent.getStringExtra("data");
            String porte    = intent.getStringExtra("porte");
            String especie  = intent.getStringExtra("especie");

            Intent form3 = new Intent(this, Form3.class);
            form3.putExtra("nome",           nome);
            form3.putExtra("idade",          idade);
            form3.putExtra("sexo",           sexo);
            form3.putExtra("data",           data);
            form3.putExtra("porte",          porte);
            form3.putExtra("especie",        especie);
            form3.putExtra("enfermidade",    enfermidade);
            form3.putExtra("obsEnfermidade", obsEnfermidade);
            form3.putExtra("local",          local);
            form3.putExtra("castrado",       castrado);

            if (modoEdicao) {
                form3.putExtra("modoEdicao", true);
                form3.putExtra("PET_OBJETO", petEdicao);
            }

            startActivity(form3);
        }

        if (view.getId() == R.id.btVoltar2) {
            startActivity(new Intent(this, Form1.class));
        }

        if (view.getId() == R.id.btFechar2) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}