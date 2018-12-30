package com.example.usuario.kprueba.utiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.usuario.kprueba.helper.HelperContactoBD;

public class GeneralCompat extends AppCompatActivity {
    protected HelperContactoBD helperContactooBD;
    protected AlertDialog.Builder builder;
    protected AlertDialog dialogoSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helperContactooBD = new HelperContactoBD(this, "bdcontactos", null, 1);
    }

    /* Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog(Context context, String titulo, String mensaje, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        builder = new AlertDialog.Builder(context);

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("ACEPTAR", ok)
                .setNegativeButton("CANCELAR", cancel);
        dialogoSimple = builder.create();
        return dialogoSimple;
    }

    protected void cerrarDialog(){
        dialogoSimple.dismiss();
    }
}
