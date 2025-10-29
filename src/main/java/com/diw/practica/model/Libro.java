package com.diw.practica.model;

public class Libro {

    private String titulo;
    private String autor;
    private String isbn;
    private Integer anioPublicacion;
    private String editorial;

    private enum estado {TRAMITADO, PENDIENTE, ESPERA};

    private estado estadoLibro;

    public Libro () {}

    public Libro(String titulo, String autor, String isbn, Integer anioPublicacion, String editorial, estado estadoLibro) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.editorial = editorial;
        this.estadoLibro = estadoLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public estado getEstadoLibro() {
        return estadoLibro;
    }

    public void setEstadoLibro(estado estadoLibro) {
        this.estadoLibro = estadoLibro;
    }
}
