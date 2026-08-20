package br.com.viniciusacoelho.invoice_issuance_system.controller;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceUpdateDTO;
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

    @PostMapping("/create/{product_id}/{product_quantity}")
    public ResponseEntity<Invoice> create(@PathVariable("product_id") Long productId, @PathVariable("product_quantity") Integer productQuantity) {
        return ResponseEntity.ok(invoiceService.create(productId, productQuantity));
    }

    @GetMapping("/read")
    public ResponseEntity<List<Invoice>> read() {
        return ResponseEntity.ok(invoiceService.read());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Invoice> update(@PathVariable("id") Long id, @Valid @RequestBody InvoiceUpdateDTO invoiceUpdateDTO) {
        return ResponseEntity.ok(invoiceService.update(id, invoiceUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Invoice> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(invoiceService.delete(id));
    }

}
