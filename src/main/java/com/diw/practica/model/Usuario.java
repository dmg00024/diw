package com.diw.practica.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

/**
 * Entidad JPA que representa a un usuario del sistema.
 * <p>
 * Un {@code Usuario} tiene un identificador único generado por la base de datos,
 * un nombre, un rol (ADMIN, PROFESOR o ALUMNO) y una lista de libros asociados
 * que han sido prestados a este usuario.
 * </p>
 *
 * @author dmg00024
 * @since 1.0
 */
@Entity
public class Usuario {

    /**
     * Identificador único del usuario.
     * <p>
     * Se genera automáticamente utilizando la estrategia {@link GenerationType#IDENTITY}.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Rol del usuario en el sistema.
     */
    public enum Rol {ADMIN, PROFESOR, ALUMNO}

    /**
     * Mapeo del rol en la base de datos como cadena.
     */
    @Enumerated (EnumType.STRING)
    private Rol rol;

    /**
     * Lista de libros prestados a este usuario.
     * <p>
     * Relación One\-To\-Many bidireccional con la entidad {@code Libro}. El atributo
     * {@code mappedBy = "prestadoA"} indica que la entidad inversa mantiene la relación.
     * Se utiliza {@link CascadeType#ALL} para propagar operaciones de persistencia.
     * La lista se inicializa para evitar {@link NullPointerException}.
     * </p>
     */
    @OneToMany(mappedBy = "prestadoA", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Libro> libros = new ArrayList<>();

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {}

    /**
     * Constructor con todos los campos relevantes (excepto la lista de libros).
     *
     * @param id     Identificador del usuario (puede ser {@code null} si se crea nuevo)
     * @param nombre Nombre del usuario
     * @param rol    Rol del usuario
     */
    public Usuario(Integer id, String nombre, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return el id del usuario, o {@code null} si no se ha establecido
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id el id a establecer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre el nombre a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol el rol a establecer
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene la lista de libros prestados a este usuario.
     * <p>
     * La lista retornada es la instancia interna; si se desea evitar su modificación
     * directa, hacer una copia al consumirla.
     * </p>
     *
     * @return la lista de libros (nunca {@code null})
     */
    public List<Libro> getLibros() {
        return libros;
    }

    /**
     * Reemplaza la lista de libros prestados a este usuario.
     *
     * @param libros nueva lista de libros (si se pasa {@code null}, se inicializa como lista vacía)
     */
    public void setLibros(List<Libro> libros) {
        this.libros = libros != null ? libros : new ArrayList<>();
    }
}