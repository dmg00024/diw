package com.diw.practica.beans;

import com.diw.practica.model.Libro;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Libro> librosDisponibles();

    List<Libro> prestamosDeUsuario(Integer usuarioId);

    Optional<Libro> solicitarPrestamo(Integer usuarioId, Integer libroId);

    Optional<Libro> devolverPrestamo(Integer usuarioId, Integer libroId);
}
