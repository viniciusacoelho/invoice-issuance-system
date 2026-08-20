package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice create(InvoiceDTO invoiceDTO) {
        if (invoiceDTO.products() == null) {
            return null; // TODO: It is not allowed to create an invoice without products.
        }
        Invoice invoice = Invoice.builder()
                .sequentialNumbering(invoiceDTO.sequentialNumbering())
                .products(invoiceDTO.products())
                .status(Invoice.Status.OPEN)
                .build();
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> read() {
        if (isInvoice()) {
            return invoiceRepository.findAll();
        }
        return null;
    }

    public Invoice update(Long id, InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = findById(id);
        invoice.setSequentialNumbering(invoiceUpdateDTO.sequentialNumbering());
        invoice.setProducts(invoiceUpdateDTO.products());
        return invoiceRepository.save(invoice);
    }

    public Invoice delete(Long id) {
        hasInvoice(id);
        invoiceRepository.deleteById(id);
        return null;
    }

    private Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Invoice.class.getName()));
    }

    private void hasInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new NotFoundException(Invoice.class.getName());
        }
    }

    private boolean isInvoice() {
        return invoiceRepository.count() > 0;
    }

}
