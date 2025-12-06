package com.diw.practica.beans;

    import com.diw.practica.model.Libro;
    import com.diw.practica.model.Usuario;

    import java.util.List;
    import java.util.Optional;

    /**
     * Interfaz de servicio para operaciones administrativas sobre usuarios y libros.
     *
     * <p>Esta interfaz define las operaciones básicas de gestión que el componente
     * administrador debe exponer: registro y listado de usuarios, y CRUD básico de libros.
     * Las implementaciones concretas deben encargarse de la persistencia, validación
     * y manejo de transacciones.</p>
     *
     * <p>Contratos generales:</p>
     * <ul>
     *   <li>Los parámetros de entrada no deben ser nulos salvo que se indique explícitamente.</li>
     *   <li>Los métodos que retornan {@code Optional} usan {@code Optional.empty()} para indicar
     *       ausencia del recurso.</li>
     *   <li>Las implementaciones pueden lanzar excepciones en tiempo de ejecución (por ejemplo,
     *       violaciones de integridad, errores de persistencia). Documentar y manejar dichas
     *       excepciones en la implementación concreta.</li>
     * </ul>
     *
     * @author
     */
    public interface AdminService {

        /**
         * Registra un nuevo usuario en el sistema.
         *
         * <p>Se espera que la implementación valide los datos del {@code usuario},
         * compruebe duplicados según la política de la aplicación (por ejemplo, por
         * correo o nombre de usuario) y persista la entidad.</p>
         *
         * @param usuario el objeto {@link Usuario} con los datos a registrar; no debe ser {@code null}
         * @return el {@link Usuario} persistido, normalmente con identificador asignado
         * @throws IllegalArgumentException si {@code usuario} es {@code null} o contiene datos inválidos
         * @throws RuntimeException si ocurre un error de persistencia u otra excepción no controlada
         */
        Usuario registrarUsuario(Usuario usuario);

        /**
         * Lista todos los usuarios registrados en el sistema.
         *
         * <p>El resultado no debe ser {@code null}. Si no existen usuarios, se debe
         * retornar una lista vacía.</p>
         *
         * @return lista no {@code null} de {@link Usuario}; puede estar vacía
         * @throws RuntimeException si ocurre un error al recuperar los datos
         */
        List<Usuario> listarUsuarios();

        /**
         * Registra un nuevo libro en el sistema.
         *
         * <p>La implementación debe validar los datos del {@code libro} y persistirlo.
         * Se espera que devuelva la entidad persistida (con id si aplica).</p>
         *
         * @param libro el {@link Libro} a registrar; no debe ser {@code null}
         * @return el {@link Libro} persistido con sus campos actualizados (p. ej. id)
         * @throws IllegalArgumentException si {@code libro} es {@code null} o inválido
         * @throws RuntimeException si ocurre un error durante la persistencia
         */
        Libro registrarLibro(Libro libro);

        /**
         * Actualiza un libro existente.
         *
         * <p>Busca el libro por su identificador {@code libroId} y, si existe, aplica
         * los cambios indicados en {@code libroActualizado}. No se presupone que todos
         * los campos de {@code libroActualizado} estén completos; la semántica exacta
         * (reemplazo total vs. parche) depende de la implementación concreta.</p>
         *
         * @param libroId           identificador del libro a actualizar; no debe ser {@code null}
         * @param libroActualizado  objeto con los nuevos datos del libro; no debe ser {@code null}
         * @return {@link Optional} que contiene el {@link Libro} actualizado si la entidad existía;
         *         {@code Optional.empty()} si no se encontró ningún libro con {@code libroId}
         * @throws IllegalArgumentException si {@code libroId} o {@code libroActualizado} son {@code null}
         * @throws RuntimeException si ocurre un error durante la actualización
         */
        Optional<Libro> actualizarLibro(Integer libroId, Libro libroActualizado);

        /**
         * Elimina un libro por su identificador.
         *
         * <p>Si el libro existe y la eliminación se realiza correctamente, se debe retornar {@code true}.
         * Si no existe, se debe retornar {@code false}. Las implementaciones deben encargarse de la
         * coherencia referencial (por ejemplo, comprobación de préstamos activos) antes de eliminar.</p>
         *
         * @param libroId identificador del libro a eliminar; no debe ser {@code null}
         * @return {@code true} si el libro fue encontrado y eliminado; {@code false} si no se encontró
         * @throws IllegalArgumentException si {@code libroId} es {@code null}
         * @throws RuntimeException si ocurre un error en la operación de eliminación
         */
        boolean eliminarLibro(Integer libroId);

        /**
         * Lista todos los libros disponibles en el sistema.
         *
         * <p>El resultado no debe ser {@code null}. Si no hay libros registrados, se debe
         * retornar una lista vacía.</p>
         *
         * @return lista no {@code null} de {@link Libro}; puede estar vacía
         * @throws RuntimeException si ocurre un error al recuperar los datos
         */
        List<Libro> listarLibros();
    }