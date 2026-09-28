package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.LoginRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.response.SessionResponseDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.Address;
import br.com.viniciusacoelho.invoice_issuance_system.service.AddressService;
import br.com.viniciusacoelho.invoice_issuance_system.service.LoginService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public String init() {
        return "Seja Bem-vindo ao Sistema de Emissão de Notas Fiscais!";
    }

    @GetMapping("/home")
    public String home() {
        return init();
    }

    @PostMapping("/login")
    public ResponseEntity<SessionResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.accepted().body(loginService.login(loginRequestDTO));
    }

// TODO:
//    @PostMapping("/logout")
//    public ResponseEntity<User> logout(@Valid @RequestBody LoginDTO loginDTO) {
//        return ResponseEntity.accepted().body(userService.login(loginDTO));
//    }

    // TODO: Remove it, because it's just a endpoint for test addresses
    @GetMapping("/address/{cep}")
    public Address findByCep(@PathVariable("cep") String cep) {
        return addressService.findByCep(cep);
    }

}
