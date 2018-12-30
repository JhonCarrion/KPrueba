package com.example.usuario.kprueba.modelo;

public class Contacto {

    private int id_contactos;
    private String nombre, apellido, numero, imagen;

    public Contacto() {
    }

    public Contacto(String nombre, String apellido, String numero, String imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numero = numero;
        this.imagen = imagen;
    }

    public int getId_contactos() {
        return id_contactos;
    }

    public void setId_contactos(int id_contactos) {
        this.id_contactos = id_contactos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", numero='" + numero + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
