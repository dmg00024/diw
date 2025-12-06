package com.diw.practica.beans;

import com.diw.practica.model.Libro;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones relacionadas con usuarios y préstamos de libros.
 *
 * <p>Define las operaciones básicas que deben ofrecer las implementaciones:
 * listar libros disponibles, listar préstamos de un usuario, solicitar un préstamo
 * y devolver un préstamo.</p>
 *
 * @see com.diw.practica.model.Libro
 * @since 1.0
 */
public interface UsuarioService {

    /**
     * Obtiene todos los libros que actualmente están disponibles para préstamo.
     *
     * <p>La lista devuelta no debe ser {@code null}; puede ser vacía si no hay libros disponibles.</p>
     *
     * @return lista de libros disponibles para préstamo
     */
    List<Libro> librosDisponibles();

    /**
     * Recupera los libros que tiene actualmente prestados un usuario identificado por {@code usuarioId}.
     *
     * <p>Si el usuario no tiene préstamos, se debe devolver una lista vacía.</p>
     *
     * @param usuarioId identificador del usuario cuyos préstamos se desean consultar; no debe ser {@code null}
     * @return lista de libros prestados al usuario (vacía si no tiene préstamos)
     * @throws IllegalArgumentException si {@code usuarioId} es {@code null}
     */
    List<Libro> prestamosDeUsuario(Integer usuarioId);

    /**
     * Solicita el préstamo de un libro para un usuario.
     *
     * <p>Si la solicitud se procesa correctamente y el libro queda prestado, se devuelve un
     * {@link Optional} con el {@link Libro} prestado. Si la solicitud no puede completarse
     * (por ejemplo, el libro no existe, no está disponible o el usuario no puede tomarlo),
     * se devuelve {@link Optional#empty()}.</p>
     *
     * @param usuarioId identificador del usuario solicitante; no debe ser {@code null}
     * @param libroId   identificador del libro a solicitar; no debe ser {@code null}
     * @return {@code Optional} con el libro prestado si la operación fue exitosa, o {@code Optional.empty()} en caso contrario
     * @throws IllegalArgumentException si {@code usuarioId} o {@code libroId} son {@code null}
     */
    Optional<Libro> solicitarPrestamo(Integer usuarioId, Integer libroId);

    /**
     * Devuelve un libro que tenía prestado un usuario.
     *
     * <p>Si la devolución se procesa correctamente y el préstamo se cierra, se devuelve un
     * {@link Optional} con el {@link Libro} devuelto. Si no existía dicho préstamo o la
     * devolución no puede realizarse, se devuelve {@link Optional#empty()}.</p>
     *
     * @param usuarioId identificador del usuario que devuelve el libro; no debe ser {@code null}
     * @param libroId   identificador del libro a devolver; no debe ser {@code null}
     * @return {@code Optional} con el libro devuelto si la operación fue exitosa, o {@code Optional.empty()} en caso contrario
     * @throws IllegalArgumentException si {@code usuarioId} o {@code libroId} son {@code null}
     */
    Optional<Libro> devolverPrestamo(Integer usuarioId, Integer libroId);
}