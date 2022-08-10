package com.lucascalderon1.listadetarefas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lucascalderon1.listadetarefas.R;
import com.lucascalderon1.listadetarefas.helper.TarefaDAO;
import com.lucascalderon1.listadetarefas.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);


        editTarefa = findViewById(R.id.textTarefa);

        //recuperar tarefa para editar
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //config cx de texto
        if (tarefaAtual != null) {
            editTarefa.setText(tarefaAtual.getNomeTarefa());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar:
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());



                if (tarefaAtual != null) {
                    String nomeTarefa = editTarefa.getText().toString();

                    if (!nomeTarefa.isEmpty()) {

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar banco da dados

                        if (tarefaDAO.atualizar(tarefa)) {
                            finish();
                            Toast.makeText(this, "Sucesso ao atualizar tarefa!", Toast.LENGTH_SHORT).show();



                        } else {

                            finish();
                            Toast.makeText(this, "Erro ao atualizar tarefa!", Toast.LENGTH_SHORT).show();

                        }

                    }
                } else {
                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);

                       if ( tarefaDAO.salvar( tarefa)) {
                           finish();
                           Toast.makeText(this, "Sucesso a o salvar tarefa!", Toast.LENGTH_SHORT).show();
                       } else
                           Toast.makeText(this, "Erro ao salvar tarefa!", Toast.LENGTH_SHORT).show();
                           



                    }

                }







                break;
    }
        return super.onOptionsItemSelected(item);
    }
}