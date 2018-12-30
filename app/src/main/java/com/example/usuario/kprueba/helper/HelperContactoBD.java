package com.example.usuario.kprueba.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.usuario.kprueba.modelo.Contacto;

import java.util.ArrayList;
import java.util.List;


public class HelperContactoBD extends SQLiteOpenHelper {

    private String respuestaCSV;

    public HelperContactoBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        respuestaCSV = "";
        db.execSQL("CREATE TABLE contacto (id_contactos INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR NOT NULL, apellido VARCHAR NOT NULL, numero VARCHAR NOT NULL, imagen VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertar(Contacto contacto) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", contacto.getNombre());
        valores.put("apellido", contacto.getApellido());
        valores.put("numero", contacto.getNumero());
        valores.put("imagen", contacto.getImagen());
        return this.getWritableDatabase().insert("contacto", null, valores);
    }

    public int editar(String id, Contacto cont) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", cont.getNombre());
        valores.put("apellido", cont.getApellido());
        valores.put("numero", cont.getNumero());
        valores.put("imagen", cont.getImagen());
        return this.getWritableDatabase().update("contacto", valores, "numero = '" + id + "'", null);
    }

    public void eliminar() {
        this.getWritableDatabase().delete("contacto", null, null);
    }

    public void eliminar(String codigoBuscar) {
        this.getWritableDatabase().delete("contacto", "numero = '" + codigoBuscar + "'", null);
    }

    public List<Contacto> leer() {
        return llenarLista(this.getReadableDatabase().rawQuery("SELECT * FROM contacto", null));
    }

    public List<Contacto> leer(int opt, String codigoBuscar) {
        if (opt == 1) {
            return llenarLista(this.getReadableDatabase().rawQuery("SELECT * FROM contacto WHERE nombre like% '" + codigoBuscar + "'", null));
        } else {
            return llenarLista(this.getReadableDatabase().rawQuery("SELECT * FROM contacto WHERE  numero = '" + codigoBuscar + "'", null));
        }
    }

    private List<Contacto> llenarLista(Cursor cursor) {
        List<Contacto> lista = new ArrayList<Contacto>();
        if (cursor.moveToFirst()) {
            respuestaCSV = "";
            do {
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
                String numero = cursor.getString(cursor.getColumnIndex("numero"));
                String imagen = cursor.getString(cursor.getColumnIndex("imagen"));
                lista.add(new Contacto(nombre, apellido, numero, imagen));
                respuestaCSV += nombre + "," + apellido + "," + numero + "," + imagen + ";";
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public String getCSV() {
        return respuestaCSV;
    }
}
