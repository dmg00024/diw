package com.diw.practica.contoller;

import com.diw.practica.beans.UsuarioService;
import com.diw.practica.model.Libro;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('USER')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/libros/disponibles")
    public List<Libro> librosDisponibles() {
        return usuarioService.librosDisponibles();
    }

    @GetMapping("/{usuarioId}/prestamos")
    public ResponseEntity<List<Libro>> prestamosDeUsuario(@PathVariable Integer usuarioId) {
        List<Libro> prestamos = usuarioService.prestamosDeUsuario(usuarioId);
        if (prestamos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prestamos);
    }

    @PostMapping("/{usuarioId}/prestamos/{libroId}")
    public ResponseEntity<Libro> solicitarPrestamo(
            @PathVariable Integer usuarioId,
            @PathVariable Integer libroId
    ) {
        return usuarioService.solicitarPrestamo(usuarioId, libroId)
                .map(libro -> ResponseEntity.status(201).body(libro))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{usuarioId}/devoluciones/{libroId}")
    public ResponseEntity<Libro> devolverPrestamo(
            @PathVariable Integer usuarioId,
            @PathVariable Integer libroId
    ) {
        return usuarioService.devolverPrestamo(usuarioId, libroId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
