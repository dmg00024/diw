package com.diw.practica.beans;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import com.diw.practica.repository.LibroRepository;
import com.diw.practica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public AdminServiceImpl(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Libro registrarLibro(Libro libro) {
        if (libro.getEstadoLibro() == null) {
            libro.setEstadoLibro(Libro.Estado.DISPONIBLE);
        }
        return libroRepository.save(libro);
    }

    @Override
    public Optional<Libro> actualizarLibro(Integer libroId, Libro libroActualizado) {
        return libroRepository.findById(libroId).map(libroExistente -> {
            libroExistente.setTitulo(libroActualizado.getTitulo());
            libroExistente.setAutor(libroActualizado.getAutor());
            libroExistente.setIsbn(libroActualizado.getIsbn());
            libroExistente.setAnioPublicacion(libroActualizado.getAnioPublicacion());
            libroExistente.setEditorial(libroActualizado.getEditorial());
            if (libroActualizado.getEstadoLibro() != null) {
                libroExistente.setEstadoLibro(libroActualizado.getEstadoLibro());
            }
            return libroRepository.save(libroExistente);
        });
    }

    @Override
    public boolean eliminarLibro(Integer libroId) {
        return libroRepository.findById(libroId).map(libro -> {
            libroRepository.delete(libro);
            return true;
        }).orElse(false);
    }

    @Override
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }
}
