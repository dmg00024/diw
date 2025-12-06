package com.diw.practica.beans;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import com.diw.practica.repository.LibroRepository;
import com.diw.practica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements  UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    public List<Libro> librosDisponibles() {
        return libroRepository.findByEstadoLibro(Libro.Estado.DISPONIBLE);
    }

    @Override
    public List<Libro> prestamosDeUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(Usuario::getLibros)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Libro> solicitarPrestamo(Integer usuarioId, Integer libroId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Libro> libro = libroRepository.findById(libroId);

        if (usuario.isEmpty() || libro.isEmpty()) {
            return Optional.empty();
        }

        Libro libroSolicitado = libro.get();
        if (libroSolicitado.getEstadoLibro() != Libro.Estado.DISPONIBLE) {
            return Optional.empty();
        }

        libroSolicitado.setEstadoLibro(Libro.Estado.PRESTADO);
        libroSolicitado.setPrestadoA(usuario.get());
        usuario.get().getLibros().add(libroSolicitado);

        libroRepository.save(libroSolicitado);
        usuarioRepository.save(usuario.get());

        return Optional.of(libroSolicitado);
    }

    @Override
    public Optional<Libro> devolverPrestamo(Integer usuarioId, Integer libroId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Libro> libro = libroRepository.findById(libroId);

        if (usuario.isEmpty() || libro.isEmpty()) {
            return Optional.empty();
        }

        Libro libroPrestado = libro.get();
        if (libroPrestado.getPrestadoA() == null || !libroPrestado.getPrestadoA().getId().equals(usuarioId)) {
            return Optional.empty();
        }

        libroPrestado.setEstadoLibro(Libro.Estado.DISPONIBLE);
        libroPrestado.setPrestadoA(null);
        usuario.get().getLibros().removeIf(l -> l.getId().equals(libroId));

        libroRepository.save(libroPrestado);
        usuarioRepository.save(usuario.get());

        return Optional.of(libroPrestado);
    }

}
