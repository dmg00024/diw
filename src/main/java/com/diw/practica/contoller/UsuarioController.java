package com.diw.practica.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/saludo")
    public String holaUsuario() {
        return "Hola soy un usuario";
    }
}
