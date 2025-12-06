package com.diw.practica.repository;

import com.diw.practica.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    List<Libro> findByEstadoLibro(Libro.Estado estado);
}
