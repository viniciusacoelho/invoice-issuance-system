package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.service.InvoiceService;

import jakarta.validation.Valid;

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

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/create")
    public ResponseEntity<Invoice> create(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
    }

    @GetMapping("/read")
    public ResponseEntity<List<Invoice>> read() {
        return ResponseEntity.ok(invoiceService.read());
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<Invoice> update(@PathVariable("id") Long id, @Valid @RequestBody InvoiceUpdateDTO invoiceUpdateDTO) {
//        return ResponseEntity.ok(invoiceService.update(id, invoiceUpdateDTO));
//    }

    @PutMapping("/update/{id}/add")
    public ResponseEntity<Invoice> addProduct(@PathVariable("id") Long id, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        return ResponseEntity.ok(invoiceService.addProduct(id, invoiceDTO));
    }

    @PutMapping("/update/{id}/remove")
    public ResponseEntity<Invoice> removeProduct(@PathVariable("id") Long id, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        return ResponseEntity.ok(invoiceService.removeProduct(id, invoiceDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Invoice> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(invoiceService.delete(id));
    }

    @GetMapping("/find/{id}/issue")
    public ResponseEntity<Invoice> issue(@PathVariable("id") Long id) {
        return ResponseEntity.ok(invoiceService.issue(id));
    }

}
