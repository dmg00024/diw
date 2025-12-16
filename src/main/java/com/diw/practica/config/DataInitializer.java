package com.diw.practica.config;

import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import com.diw.practica.repository.LibroRepository;
import com.diw.practica.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Inicializa datos de ejemplo en la base de datos al arrancar la aplicación.
 *
 * <p>Para evitar duplicados, el proceso se ejecuta solo cuando no existen
 * usuarios ni libros almacenados.</p>
 */
@Component
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public DataInitializer(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    /**
     * Crea usuarios y libros de ejemplo para facilitar las pruebas manuales.
     */
    @PostConstruct
    @Transactional
    public void cargarDatosIniciales() {
        if (usuarioRepository.count() > 0 || libroRepository.count() > 0) {
            return;
        }

        Usuario admin = new Usuario(null, "Administradora", Usuario.Rol.ADMIN);
        Usuario profesor = new Usuario(null, "Profesorado", Usuario.Rol.PROFESOR);
        Usuario alumna = new Usuario(null, "Alumna", Usuario.Rol.ALUMNO);

        usuarioRepository.saveAll(List.of(admin, profesor, alumna));

        Libro cleanCode = new Libro(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                2008,
                "Prentice Hall",
                Libro.Estado.DISPONIBLE
        );

        Libro ddd = new Libro(
                "Domain-Driven Design",
                "Eric Evans",
                "978-0321125217",
                2003,
                "Addison-Wesley",
                Libro.Estado.PRESTADO
        );

        Libro springInAction = new Libro(
                "Spring in Action",
                "Craig Walls",
                "978-1617294945",
                2018,
                "Manning",
                Libro.Estado.RESERVADO
        );

        // Vincular el libro prestado y reservado a usuarios específicos para reflejar estados reales.
        ddd.setPrestadoA(alumna);
        alumna.getLibros().add(ddd);

        springInAction.setPrestadoA(profesor);
        profesor.getLibros().add(springInAction);

        libroRepository.saveAll(List.of(cleanCode, ddd, springInAction));
        usuarioRepository.saveAll(List.of(alumna, profesor));
    }
}