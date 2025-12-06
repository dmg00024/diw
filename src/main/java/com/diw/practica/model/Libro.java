package com.diw.practica.model;

import jakarta.persistence.*;

@Entity
public class Libro {

    public enum Estado {DISPONIBLE, PRESTADO, RESERVADO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anioPublicacion;
    private String editorial;

    @Enumerated(EnumType.STRING)
    private Estado estadoLibro = Estado.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario prestadoA;

    public Libro() {
    }

    public Libro(
            String titulo,
            String autor,
            String isbn,
            Integer anioPublicacion,
            String editorial,
            Estado estadoLibro
    ) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.editorial = editorial;
        this.estadoLibro = estadoLibro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Estado getEstadoLibro() {
        return estadoLibro;
    }

    public void setEstadoLibro(Estado estadoLibro) {
        this.estadoLibro = estadoLibro;
    }

    public Usuario getPrestadoA() {
        return prestadoA;
    }

    public void setPrestadoA(Usuario prestadoA) {
        this.prestadoA = prestadoA;
    }
}
