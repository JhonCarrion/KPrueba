package com.example.usuario.kprueba.clases;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.usuario.kprueba.Adaptador.ContactoAdapter;
import com.example.usuario.kprueba.R;
import com.example.usuario.kprueba.helper.HelperContactoBD;
import com.example.usuario.kprueba.modelo.Contacto;
import com.example.usuario.kprueba.utiles.GeneralCompat;

public class ListaContactoActivity extends GeneralCompat {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected final static int REQUEST_CODE_CONTACTO_ACTIVITY = 101;
    protected final static int REQUEST_CODE_EDIT_CONTACTO_ACTIVITY = 102;
    protected Button btnNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnNuevo = findViewById(R.id.btn_crear_contacto);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ListaContactoActivity.this, ContactoActivity.class), REQUEST_CODE_CONTACTO_ACTIVITY);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ContactoAdapter(helperContactooBD.leer());
        Log.e("respuesta", String.valueOf(mAdapter.getItemCount()));
        ((ContactoAdapter) mAdapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final CharSequence[] options = {"Editar", "Eliminar", "Cancelar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaContactoActivity.this);
                builder.setTitle("Opciones")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Contacto contacto = helperContactooBD.leer().get(mRecyclerView.getChildAdapterPosition(v));
                                if (contacto.getNumero() != null) {
                                    if (options[which].equals("Editar")) {
                                        Intent intent = new Intent(ListaContactoActivity.this, ContactoActivity.class);
                                        intent.putExtra("idCliente", String.valueOf(contacto.getNumero()));
                                        startActivityForResult(intent, REQUEST_CODE_EDIT_CONTACTO_ACTIVITY);
                                    } else if (options[which].equals("Eliminar")) {
                                        createSimpleDialog(ListaContactoActivity.this, "ATENCIÓN", "Esta seguro de Eliminar a:\n" + contacto.getNombre() + " " + contacto.getApellido(), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                helperContactooBD.eliminar(contacto.getNumero());
                                                Toast.makeText(ListaContactoActivity.this, "Contacto Eliminado", Toast.LENGTH_SHORT).show();
                                                recreate();
                                                cerrarDialog();
                                            }
                                        }, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                cerrarDialog();
                                            }
                                        }).show();
                                    } else {

                                    }
                                }
                            }
                        });
                builder.create().show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTACTO_ACTIVITY && resultCode == RESULT_OK) {
            Toast.makeText(ListaContactoActivity.this, data.getLongExtra("result", -1) == -1 ? "Error de Guardado" : "Guardado Exitoso", Toast.LENGTH_SHORT).show();
            recreate();
        }
        if (requestCode == REQUEST_CODE_EDIT_CONTACTO_ACTIVITY && resultCode == RESULT_OK) {
            Toast.makeText(ListaContactoActivity.this, data.getIntExtra("result", -1) == -1 ? "Error de Modificado" : "Modificado Exitoso", Toast.LENGTH_SHORT).show();
            recreate();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
