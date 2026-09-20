package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.LoginDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.SessionDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.User;
import br.com.viniciusacoelho.invoice_issuance_system.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String init() {
        return "Seja Bem-vindo ao Sistema de Emissão de Notas Fiscais!";
    }

    @GetMapping("/home")
    public String home() {
        return init();
    }

    @PostMapping("/login")
    public ResponseEntity<SessionDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.accepted().body(userService.login(loginDTO));
    }

// TODO:
//    @PostMapping("/logout")
//    public ResponseEntity<User> logout(@Valid @RequestBody LoginDTO loginDTO) {
//        return ResponseEntity.accepted().body(userService.login(loginDTO));
//    }

}
