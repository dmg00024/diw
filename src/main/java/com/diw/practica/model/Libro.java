package com.diw.practica.model;

public class Libro {
    private String Titulo;
    private String Autor;
    private String ISBN;
    private int AnioPublicacion;
    private String Editorial;

    private enum estado {TRAMITADO, PENDIENTE, ESPERA}

    ;

    public Libro(String titulo, String autor, String ISBN, int anioPublicacion, String editorial) {
        Titulo = titulo;
        Autor = autor;
        this.ISBN = ISBN;
        AnioPublicacion = anioPublicacion;
        Editorial = editorial;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getAnioPublicacion() {
        return AnioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        AnioPublicacion = anioPublicacion;
    }

    public String getEditorial() {
        return Editorial;
    }

    public void setEditorial(String editorial) {
        Editorial = editorial;
    }
}
