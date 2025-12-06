package com.diw.practica.contoller;

import com.diw.practica.beans.AdminService;
import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return adminService.listarUsuarios();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.status(201).body(adminService.registrarUsuario(usuario));
    }

    @GetMapping("/libros")
    public List<Libro> listarLibros() {
        return adminService.listarLibros();
    }

    @PostMapping("/libros")
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        return ResponseEntity.status(201).body(adminService.registrarLibro(libro));
    }

    @PutMapping("/libros/{libroId}")
    public ResponseEntity<Libro> actualizarLibro(
            @PathVariable Integer libroId,
            @RequestBody Libro libroActualizado
    ) {
        return adminService.actualizarLibro(libroId, libroActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/libros/{libroId}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Integer libroId) {
        return adminService.eliminarLibro(libroId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
