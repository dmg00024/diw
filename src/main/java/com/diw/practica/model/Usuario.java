package com.diw.practica.model;
import java.util.*;


public class Usuario {
    private String nombre;
    private List<Libro> libros; // Lista para almacenar los libros asociados al usuario
    enum rol {ADMIN, PROFESOR, ALUMNO};

    public Usuario() {
        this.libros = new ArrayList<>(); // Inicializo la lista de libros
    }

    public Usuario(String nombre, List<Libro> libros) {
        this.nombre = nombre;
        this.libros = libros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}