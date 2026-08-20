package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductService productService;

    public Invoice create(Long  productId, Integer productQuantity) {
        Product product = productService.findById(productId);
        productService.updateStock(productId, productQuantity);
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(setStatusOpen())
                .productQuantity(productQuantity)
                .build();
        addProduct(invoice, product);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public List<Invoice> read() {
        if (isInvoice()) {
            return invoiceRepository.findAll();
        }
        return null;
    }

    public Invoice update(Long id, InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = findById(id);
        invoice.setSequentialNumber(invoiceUpdateDTO.sequentialNumber());
//        invoice.setProducts(invoiceUpdateDTO.products());
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

    private static void addProduct(Invoice invoice, Product product) {
        invoice.getProducts().add(product);
    }

    private long calculateSequentialNumber() {
        return invoiceRepository.count() + 1;
    }

    private static Invoice.Status setStatusOpen() {
        return Invoice.Status.OPEN;
    }

}
