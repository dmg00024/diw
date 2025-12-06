package com.diw.practica.contoller;

import com.diw.practica.beans.AdminService;
import com.diw.practica.model.Libro;
import com.diw.practica.model.Usuario;
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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.headers.Header;

/**
 * Controlador para operaciones administrativas sobre usuarios y libros.
 * Todas las rutas están protegidas y requieren rol \`ADMIN\`.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Operaciones administrativas para usuarios y libros")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Lista todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios", description = "Devuelve la lista de todos los usuarios registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public List<Usuario> listarUsuarios() {
        return adminService.listarUsuarios();
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario objeto usuario a crear
     * @return usuario creado con estado 201
     */
    @PostMapping("/usuarios")
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)),
                    headers = @Header(name = "Location", description = "URI del recurso creado", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Usuario> crearUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario a crear", required = true)
            @RequestBody Usuario usuario) {
        Usuario creado = adminService.registrarUsuario(usuario);
        return ResponseEntity.status(201).body(creado);
    }

    /**
     * Lista todos los libros.
     *
     * @return lista de libros
     */
    @GetMapping("/libros")
    @Operation(summary = "Listar libros", description = "Devuelve la lista de todos los libros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de libros obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Libro.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public List<Libro> listarLibros() {
        return adminService.listarLibros();
    }

    /**
     * Crea un nuevo libro.
     *
     * @param libro libro a crear
     * @return libro creado con estado 201
     */
    @PostMapping("/libros")
    @Operation(summary = "Crear libro", description = "Crea un nuevo libro en el catálogo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class)),
                    headers = @Header(name = "Location", description = "URI del recurso creado", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Libro> crearLibro(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Libro a crear", required = true)
            @RequestBody Libro libro) {
        return ResponseEntity.status(201).body(adminService.registrarLibro(libro));
    }

    /**
     * Actualiza un libro existente.
     *
     * @param libroId          id del libro a actualizar
     * @param libroActualizado datos actualizados del libro
     * @return libro actualizado o 404 si no existe
     */
    @PutMapping("/libros/{libroId}")
    @Operation(summary = "Actualizar libro", description = "Actualiza los datos de un libro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Libro> actualizarLibro(
            @Parameter(description = "ID del libro a actualizar", required = true) @PathVariable Integer libroId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del libro actualizados", required = true)
            @RequestBody Libro libroActualizado
    ) {
        return adminService.actualizarLibro(libroId, libroActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un libro por su id.
     *
     * @param libroId id del libro a eliminar
     * @return 204 si se eliminó, 404 si no se encontró
     */
    @DeleteMapping("/libros/{libroId}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<Void> eliminarLibro(
            @Parameter(description = "ID del libro a eliminar", required = true) @PathVariable Integer libroId) {
        return adminService.eliminarLibro(libroId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}