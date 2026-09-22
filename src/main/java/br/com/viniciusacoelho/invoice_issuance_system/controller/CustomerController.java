package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.Customer;
import br.com.viniciusacoelho.invoice_issuance_system.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.create(customerDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> read() {
        return ResponseEntity.ok(customerService.read());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable("id") Long id, @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return ResponseEntity.ok(customerService.update(id, customerUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
