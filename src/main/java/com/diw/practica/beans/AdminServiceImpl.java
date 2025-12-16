package com.diw.practica.beans;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import com.diw.practica.repository.LibroRepository;
import com.diw.practica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementación del servicio administrativo encargado de operaciones CRUD
 * sobre las entidades {@link Usuario} y {@link Libro}.
 *
 * <p>Esta clase delega la persistencia en las instancias de
 * {@link UsuarioRepository} y {@link LibroRepository} inyectadas por Spring.</p>
 *
 * @author
 * @since 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    /**
     * Repositorio para operaciones sobre {@link Usuario}.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositorio para operaciones sobre {@link Libro}.
     */
    private final LibroRepository libroRepository;

    /**
     * Construye una nueva instancia de {@code AdminServiceImpl} con los
     * repositorios necesarios inyectados.
     *
     * @param usuarioRepository repositorio para la gestión de usuarios; no debe ser {@code null}
     * @param libroRepository   repositorio para la gestión de libros; no debe ser {@code null}
     */
    @Autowired
    public AdminServiceImpl(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "El repositorio de usuarios es obligatorio");
        this.libroRepository = Objects.requireNonNull(libroRepository, "El repositorio de libros es obligatorio");
    }

    /**
     * Registra un nuevo usuario en la persistencia.
     *
     * <p>Utiliza {@link UsuarioRepository#save(Object)} para almacenar el usuario.
     * Si el repositorio lanza una excepción relacionada con acceso a datos,
     * ésta se propagará al llamador.</p>
     *
     * @param usuario entidad {@link Usuario} a registrar; se espera que contenga los datos necesarios
     * @return la instancia persistida de {@link Usuario} (puede incluir campos generados como id)
     */
    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo");
        return usuarioRepository.save(usuario);
    }

    /**
     * Devuelve la lista de todos los usuarios almacenados.
     *
     * @return lista de instancias {@link Usuario}; nunca {@code null} (puede ser vacía)
     */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Registra un nuevo libro en la persistencia.
     *
     * <p>Si el campo {@code estadoLibro} del objeto proporcionado es {@code null},
     * se establece por defecto a {@link Libro.Estado#DISPONIBLE} antes de persistir.</p>
     *
     * @param libro entidad {@link Libro} a registrar
     * @return la instancia persistida de {@link Libro} (puede contener campos generados como id)
     */
    @Override
    public Libro registrarLibro(Libro libro) {
        Objects.requireNonNull(libro, "El libro no puede ser nulo");
        if (libro.getEstadoLibro() == null) {
            libro.setEstadoLibro(Libro.Estado.DISPONIBLE);
        }
        return libroRepository.save(libro);
    }

    /**
     * Actualiza los datos de un libro existente con los valores proporcionados.
     *
     * <p>Busca el libro por su identificador; si existe, actualiza los campos
     * título, autor, ISBN, año de publicación, editorial y, si se proporciona,
     * el estado del libro. Persiste los cambios y devuelve el libro actualizado.</p>
     *
     * @param libroId          identificador del libro a actualizar
     * @param libroActualizado objeto {@link Libro} que contiene los nuevos valores
     * @return {@link Optional} que contiene el libro actualizado si se encontró el libro,
     *         o {@link Optional#empty()} si no existe un libro con el identificador dado
     */
    @Override
    public Optional<Libro> actualizarLibro(Integer libroId, Libro libroActualizado) {
        Objects.requireNonNull(libroId, "El identificador del libro no puede ser nulo");
        Objects.requireNonNull(libroActualizado, "El libro actualizado no puede ser nulo");

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

    /**
     * Elimina un libro por su identificador.
     *
     * <p>Si el libro existe, se elimina y el método devuelve {@code true}.
     * Si no existe, devuelve {@code false}.</p>
     *
     * @param libroId identificador del libro a eliminar
     * @return {@code true} si el libro fue encontrado y eliminado; {@code false} en caso contrario
     */
    @Override
    public boolean eliminarLibro(Integer libroId) {
        Objects.requireNonNull(libroId, "El identificador del libro no puede ser nulo");
        return libroRepository.findById(libroId).map(libro -> {
            libroRepository.delete(libro);
            return true;
        }).orElse(false);
    }

    /**
     * Obtiene la lista de todos los libros almacenados.
     *
     * @return lista de instancias {@link Libro}; nunca {@code null} (puede ser vacía)
     */
    @Override
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }
}