package com.diw.practica.beans;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import com.diw.practica.repository.LibroRepository;
import com.diw.practica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementación de {@code UsuarioService} que gestiona operaciones relacionadas con
 * usuarios y libros en el repositorio.
 *
 * <p>Esta clase proporciona métodos para:
 * <ul>
 *     <li>Listar libros disponibles.</li>
 *     <li>Obtener los préstamos (libros) de un usuario.</li>
 *     <li>Solicitar el préstamo de un libro por un usuario.</li>
 *     <li>Devolver un libro prestado por un usuario.</li>
 * </ul>
 *
 * <p>Las operaciones que modifican entidades persisten los cambios mediante los
 * repositorios {@link UsuarioRepository} y {@link LibroRepository}.
 *
 * @see UsuarioService
 * @see UsuarioRepository
 * @see LibroRepository
 * @since 1.0
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    /**
     * Repositorio de usuarios inyectado para búsquedas y persistencia.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositorio de libros inyectado para búsquedas y persistencia.
     */
    private final LibroRepository libroRepository;

    /**
     * Crea una nueva instancia de {@code UsuarioServiceImpl} con los repositorios necesarios.
     *
     * @param usuarioRepository repositorio usado para operaciones con {@link Usuario}; no debe ser {@code null}.
     * @param libroRepository   repositorio usado para operaciones con {@link Libro}; no debe ser {@code null}.
     */
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    /**
     * Recupera la lista de libros cuyo estado es {@link Libro.Estado#DISPONIBLE}.
     *
     * <p>Este método delega la consulta al {@link LibroRepository}.
     *
     * @return lista de libros disponibles; si no hay, se devuelve una lista vacía.
     */
    @Override
    public List<Libro> librosDisponibles() {
        return libroRepository.findByEstadoLibro(Libro.Estado.DISPONIBLE);
    }

    /**
     * Obtiene la lista de libros actualmente prestados a un usuario determinado.
     *
     * <p>Si el usuario no existe, se devuelve una lista vacía.
     *
     * @param usuarioId identificador del usuario cuyo listado de préstamos se solicita; puede ser {@code null}.
     * @return lista de {@link Libro} asociados al usuario, o lista vacía si el usuario no existe.
     */
    @Override
    public List<Libro> prestamosDeUsuario(Integer usuarioId) {
        Objects.requireNonNull(usuarioId, "El identificador del usuario no puede ser nulo");

        return usuarioRepository.findById(usuarioId)
                .map(Usuario::getLibros)
                .orElse(Collections.emptyList());
    }

    /**
     * Solicita el préstamo de un libro para un usuario.
     *
     * <p>Flujo:
     * <ol>
     *     <li>Verifica que el usuario y el libro existan.</li>
     *     <li>Verifica que el libro esté en estado {@link Libro.Estado#DISPONIBLE}.</li>
     *     <li>Actualiza el estado del libro a {@link Libro.Estado#PRESTADO}, asigna el usuario
     *     como prestatario y añade el libro a la colección de libros del usuario.</li>
     *     <li>Persiste los cambios en ambos repositorios y devuelve el libro actualizado.</li>
     * </ol>
     *
     * <p>Si el usuario o el libro no existen, o el libro no está disponible, se devuelve {@link Optional#empty()}.
     *
     * @param usuarioId identificador del usuario que solicita el préstamo.
     * @param libroId   identificador del libro solicitado.
     * @return {@link Optional} con el {@link Libro} prestado tras la operación, o {@code Optional.empty()} si no se pudo realizar.
     */
    @Override
    public Optional<Libro> solicitarPrestamo(Integer usuarioId, Integer libroId) {
        Objects.requireNonNull(usuarioId, "El identificador del usuario no puede ser nulo");
        Objects.requireNonNull(libroId, "El identificador del libro no puede ser nulo");

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

    /**
     * Procesa la devolución de un libro prestado por un usuario.
     *
     * <p>Flujo:
     * <ol>
     *     <li>Verifica que el usuario y el libro existan.</li>
     *     <li>Verifica que el libro esté efectivamente prestado al usuario indicado.</li>
     *     <li>Actualiza el estado del libro a {@link Libro.Estado#DISPONIBLE}, elimina la referencia
     *     al prestatario y elimina el libro de la colección de préstamos del usuario.</li>
     *     <li>Persiste los cambios en ambos repositorios y devuelve el libro actualizado.</li>
     * </ol>
     *
     * <p>Si el usuario o libro no existen, o si el libro no está prestado al usuario indicado,
     * se devuelve {@link Optional#empty()}.
     *
     * @param usuarioId identificador del usuario que realiza la devolución.
     * @param libroId   identificador del libro que se devuelve.
     * @return {@link Optional} con el {@link Libro} actualizado tras la devolución, o {@code Optional.empty()} si no se pudo procesar.
     */
    @Override
    public Optional<Libro> devolverPrestamo(Integer usuarioId, Integer libroId) {
        Objects.requireNonNull(usuarioId, "El identificador del usuario no puede ser nulo");
        Objects.requireNonNull(libroId, "El identificador del libro no puede ser nulo");

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