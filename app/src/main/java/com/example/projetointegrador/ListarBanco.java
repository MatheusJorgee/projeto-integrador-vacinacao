package com.example.projetointegrador;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.List;

public class ListarBanco extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_banco);

        listView = findViewById(R.id.listView);

        BancoControllerAnimais controller = new BancoControllerAnimais(this);

        List<String> dados = controller.listar();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dados
        );

        listView.setAdapter(adapter);
    }
}
