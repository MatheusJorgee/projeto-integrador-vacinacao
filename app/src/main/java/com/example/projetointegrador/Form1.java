package com.example.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Form1 extends AppCompatActivity implements View.OnClickListener {

    Button btProximo;
    ImageButton btVoltar, btFechar;
    EditText txtNome, txtIdade, txtData;
    RadioGroup rgPorte;
    Spinner spSexo, spEspecie;
    ScrollView form1;

    Pet petEdicao = null;
    boolean modoEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        btProximo = findViewById(R.id.btProximo);
        btVoltar  = findViewById(R.id.btVoltar);
        btFechar  = findViewById(R.id.btFechar);

        btProximo.setOnClickListener(this);
        btVoltar.setOnClickListener(this);
        btFechar.setOnClickListener(this);

        txtNome   = findViewById(R.id.txtNome);
        txtIdade  = findViewById(R.id.txtIdade);
        txtData   = findViewById(R.id.txtData);
        rgPorte   = findViewById(R.id.rgPorte);
        spSexo    = findViewById(R.id.spSexo);
        spEspecie = findViewById(R.id.spEspecie);
        form1     = findViewById(R.id.form1);

        String[] sexo = getResources().getStringArray(R.array.sexo);
        spSexo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sexo));

        String[] especie = getResources().getStringArray(R.array.especie);
        spEspecie.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, especie));

        modoEdicao = getIntent().getBooleanExtra("modoEdicao", false);
        if (modoEdicao) {
            petEdicao = (Pet) getIntent().getSerializableExtra("PET_OBJETO");
            preencherCampos(sexo, especie);
            btProximo.setText("Próximo (Edição)");
        }
    }

    private void preencherCampos(String[] sexoArr, String[] especieArr) {
        if (petEdicao == null) return;

        txtNome.setText(petEdicao.getNome());
        txtIdade.setText(String.valueOf(petEdicao.getIdade()));
        txtData.setText(petEdicao.getData());

        switch (petEdicao.getPorte()) {
            case "Grande":  rgPorte.check(R.id.rbGrande);  break;
            case "Médio":   rgPorte.check(R.id.rgMedio);   break;
            case "Pequeno": rgPorte.check(R.id.rgPequeno); break;
        }

        for (int i = 0; i < sexoArr.length; i++) {
            if (sexoArr[i].equals(petEdicao.getSexo())) {
                spSexo.setSelection(i); break;
            }
        }

        for (int i = 0; i < especieArr.length; i++) {
            if (especieArr[i].equals(petEdicao.getEspecie())) {
                spEspecie.setSelection(i); break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btProximo) {

            String nome     = txtNome.getText().toString();
            String idadeStr = txtIdade.getText().toString();
            String data     = txtData.getText().toString();
            String sexo     = spSexo.getSelectedItem().toString();
            String especie  = spEspecie.getSelectedItem().toString();

            int idade = 0;
            if (!idadeStr.isEmpty()) idade = Integer.parseInt(idadeStr);

            int idSelecionado = rgPorte.getCheckedRadioButtonId();
            if (idSelecionado == -1) {
                Toast.makeText(this, "Selecione o porte!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton radioSelecionado = findViewById(idSelecionado);
            String porte = radioSelecionado.getText().toString();

            Intent intent = new Intent(this, Form2.class);
            intent.putExtra("nome",    nome);
            intent.putExtra("idade",   idade);
            intent.putExtra("sexo",    sexo);
            intent.putExtra("data",    data);
            intent.putExtra("porte",   porte);
            intent.putExtra("especie", especie);

            if (modoEdicao) {
                intent.putExtra("modoEdicao", true);
                intent.putExtra("PET_OBJETO", petEdicao);
            }

            startActivity(intent);
        }

        if (view.getId() == R.id.btVoltar) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (view.getId() == R.id.btFechar) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}