package com.diw.practica.beans;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Usuario registrarUsuario(Usuario usuario);

    List<Usuario> listarUsuarios();

    Libro registrarLibro(Libro libro);

    Optional<Libro> actualizarLibro(Integer libroId, Libro libroActualizado);

    boolean eliminarLibro(Integer libroId);

    List<Libro> listarLibros();
}
