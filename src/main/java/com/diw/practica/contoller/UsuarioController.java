package com.diw.practica.contoller;

import com.diw.practica.beans.UsuarioService;
import com.diw.practica.model.Libro;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.headers.Header;

/**
 * Controlador para operaciones disponibles a usuarios con rol \`USER\`.
 * Maneja listados de libros, préstamos y devoluciones.
 */
@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Usuario", description = "Operaciones para usuarios (préstamos, devoluciones, listado)")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Devuelve la lista de libros disponibles para préstamo.
     *
     * @return lista de libros disponibles
     */
    @GetMapping(path = "/libros/disponibles", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar libros disponibles", description = "Devuelve la lista de libros que están disponibles para préstamo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de libros obtenida correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Libro.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public List<Libro> librosDisponibles() {
        return usuarioService.librosDisponibles();
    }

    /**
     * Obtiene los préstamos de un usuario.
     *
     * @param usuarioId id del usuario
     * @return lista de libros prestados o 404 si no tiene préstamos
     */
    @GetMapping(path = "/{usuarioId}/prestamos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar préstamos de usuario", description = "Devuelve los libros prestados a un usuario dado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Préstamos obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Libro.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron préstamos para el usuario", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<List<Libro>> prestamosDeUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Integer usuarioId) {
        List<Libro> prestamos = usuarioService.prestamosDeUsuario(usuarioId);
        if (prestamos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prestamos);
    }

    /**
     * Solicita el préstamo de un libro para un usuario.
     *
     * @param usuarioId id del usuario que solicita
     * @param libroId   id del libro a solicitar
     * @return libro solicitado con estado 201 o 400 si no es posible
     */
    @PostMapping( "/{usuarioId}/prestamos/{libroId}")
    @Operation(summary = "Solicitar préstamo", description = "Solicita el préstamo de un libro para el usuario indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo solicitado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)),
                    headers = @Header(name = "Location", description = "URI del recurso creado", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o libro no disponible", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Libro> solicitarPrestamo(
            @Parameter(description = "ID del usuario que solicita", required = true) @PathVariable Integer usuarioId,
            @Parameter(description = "ID del libro a solicitar", required = true) @PathVariable Integer libroId
    ) {
        return usuarioService.solicitarPrestamo(usuarioId, libroId)
                .map(libro -> ResponseEntity.status(201).body(libro))
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Devuelve un libro prestado por un usuario.
     *
     * @param usuarioId id del usuario que devuelve
     * @param libroId   id del libro a devolver
     * @return libro devuelto o 400 si la devolución es inválida
     */
    @PostMapping(path = "/{usuarioId}/devoluciones/{libroId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Devolver préstamo", description = "Registra la devolución de un libro por parte del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolución procesada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "400", description = "Devolución inválida", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Libro> devolverPrestamo(
            @Parameter(description = "ID del usuario que devuelve", required = true) @PathVariable Integer usuarioId,
            @Parameter(description = "ID del libro a devolver", required = true) @PathVariable Integer libroId
    ) {
        return usuarioService.devolverPrestamo(usuarioId, libroId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}