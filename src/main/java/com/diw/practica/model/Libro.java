package com.diw.practica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa un libro en el sistema.
 * <p>
 * Contiene los metadatos básicos de un libro (título, autor, ISBN, año de publicación,
 * editorial) y el estado actual (disponible, prestado, reservado). Si el libro está
 * prestado, la relación {@code prestadoA} referencia al {@link Usuario} que lo tiene.
 * </p>
 *
 * @author dmg00024
 * @version 1.0
 * @since 1.0
 */
@Entity
public class Libro {

    /**
     * Estados posibles de un libro.
     * <ul>
     *     <li>{@link #DISPONIBLE} — El libro está disponible para préstamo.</li>
     *     <li>{@link #PRESTADO} — El libro está actualmente prestado a un usuario.</li>
     *     <li>{@link #RESERVADO} — El libro está reservado por un usuario y no está disponible.</li>
     * </ul>
     */
    public enum Estado {DISPONIBLE, PRESTADO, RESERVADO}

    /**
     * Identificador único generado por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Título del libro.
     */
    private String titulo;

    /**
     * Autor o autores del libro.
     */
    private String autor;

    /**
     * Código ISBN del libro.
     */
    private String isbn;

    /**
     * Año de publicación.
     */
    private Integer anioPublicacion;

    /**
     * Editorial responsable de la publicación.
     */
    private String editorial;

    /**
     * Estado actual del libro.
     * <p>
     * Se persiste como cadena en la base de datos gracias a {@link EnumType#STRING}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    private Estado estadoLibro = Estado.DISPONIBLE;

    /**
     * Usuario al que está prestado el libro.
     * <p>
     * Relación muchos-a-uno con la entidad {@link Usuario}. Se utiliza {@link FetchType#LAZY}
     * para evitar cargar el usuario hasta que sea necesario.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference (value = "usuario-libros")
    private Usuario prestadoA;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Libro() {
    }

    /**
     * Construye una instancia de {@code Libro} con los datos principales.
     *
     * @param titulo          título del libro
     * @param autor           autor o autores del libro
     * @param isbn            código ISBN
     * @param anioPublicacion año de publicación
     * @param editorial       editorial del libro
     * @param estadoLibro     estado inicial del libro
     */
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

    /**
     * Obtiene el identificador único del libro.
     *
     * @return id del libro, puede ser {@code null} si no ha sido persistido aún
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del libro.
     * <p>
     * En la mayoría de los casos este valor es gestionado por JPA y no debe asignarse manualmente.
     * </p>
     *
     * @param id identificador a establecer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo título a establecer
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return autor del libro
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     *
     * @param autor autor a establecer
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene el ISBN del libro.
     *
     * @return ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el ISBN del libro.
     *
     * @param isbn ISBN a establecer
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtiene el año de publicación.
     *
     * @return año de publicación, o {@code null} si no está especificado
     */
    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    /**
     * Establece el año de publicación.
     *
     * @param anioPublicacion año a establecer
     */
    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return editorial del libro
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Establece la editorial del libro.
     *
     * @param editorial editorial a establecer
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene el estado actual del libro.
     *
     * @return estado del libro
     */
    public Estado getEstadoLibro() {
        return estadoLibro;
    }

    /**
     * Establece el estado del libro.
     *
     * @param estadoLibro estado a establecer
     */
    public void setEstadoLibro(Estado estadoLibro) {
        this.estadoLibro = estadoLibro;
    }

    /**
     * Obtiene el usuario al que está prestado el libro.
     *
     * @return usuario que tiene el libro prestado, o {@code null} si no está prestado
     */
    public Usuario getPrestadoA() {
        return prestadoA;
    }

    /**
     * Establece el usuario al que se presta el libro.
     *
     * @param prestadoA usuario destinatario del préstamo, o {@code null} para indicar que no está prestado
     */
    public void setPrestadoA(Usuario prestadoA) {
        this.prestadoA = prestadoA;
    }
}