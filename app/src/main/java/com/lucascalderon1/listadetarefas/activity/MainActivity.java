package com.lucascalderon1.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucascalderon1.listadetarefas.R;
import com.lucascalderon1.listadetarefas.adapter.TarefaAdapter;
import com.lucascalderon1.listadetarefas.databinding.ActivityMainBinding;
import com.lucascalderon1.listadetarefas.helper.DbHelper;
import com.lucascalderon1.listadetarefas.helper.RecyclerItemClickListener;
import com.lucascalderon1.listadetarefas.helper.TarefaDAO;
import com.lucascalderon1.listadetarefas.model.Tarefa;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        DbHelper db = new DbHelper(getApplicationContext());
        ContentValues cv = new ContentValues();
        db.getWritableDatabase().insert("tarefas", null, cv );


        //add evento de cliques

        recyclerView.addOnItemTouchListener(
               new  RecyclerItemClickListener(
                       getApplicationContext(),
                       recyclerView,
                       new RecyclerItemClickListener.OnItemClickListener() {
                           @Override
                           public void onItemClick(View view, int position) {
                               //recuperar tarefa para editar
                               Tarefa tarefaSelecionada = listaTarefas.get(position);

                               Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                               intent.putExtra("tarefaSelecionada", tarefaSelecionada );

                               startActivity(intent);



                           }

                           @Override
                           public void onLongItemClick(View view, int position) {
                               //recuperar para deletar
                               tarefaSelecionada = listaTarefas.get(position);



                               AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                               //config mensagem
                               dialog.setTitle("Confirmar exclusão");
                               dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?" );
                               dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                       TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                       if (tarefaDAO.deletar(tarefaSelecionada)){

                                           carregarListaTarefas();
                                           Toast.makeText(getApplicationContext(),
                                                   "Sucesso ao excluir tarefa!",
                                                   Toast.LENGTH_SHORT).show();

                                       } else  {
                                          Toast.makeText(getApplicationContext(),
                                                  "Erro ao excluir tarefa!",
                                                  Toast.LENGTH_SHORT).show();

                                       }

                                   }
                               });

                               dialog.setNegativeButton("Não", null  );

                               dialog.create();
                               dialog.show();



                           }

                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                           }
                       }
               )
        );




        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void carregarListaTarefas () {

        //listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO( getApplicationContext() );
        listaTarefas = tarefaDAO.listar();

        //exibir lista de tarefas no recyclerView

        //config adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);


        // config RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);
    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
