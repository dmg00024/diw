package com.diw.practica.contoller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/saludo")
    public String holaUsuario() {
        return "Hola soy el administrador";
    }
}
