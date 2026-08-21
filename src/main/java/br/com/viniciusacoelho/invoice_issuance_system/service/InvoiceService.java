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

    public Invoice create(Long productId, Integer productQuantity) {
        Product product = productService.findById(productId);
        productService.removeStock(productId, productQuantity);
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(setStatusOpen())
                .build();
        addProduct(invoice, product, productQuantity);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public List<Invoice> read() {
        if (isInvoice()) {
            return invoiceRepository.findAll();
        }
        return null;
    }

    public Invoice update(Invoice invoice) {
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

    public void addProduct(Invoice invoice, Product product, Integer productQuantity) {
        if (productQuantity > 0) {
            invoice.setProductQuantity(invoice.getProductQuantity() + productQuantity);
            invoice.getProducts().add(product);
        }
    }

    public Invoice addProduct(Long invoiceId, Long productId, Integer productQuantity) {
        Invoice invoice = findById(invoiceId);
        Product product = productService.findById(productId);
        addProductQuantity(invoice, productQuantity);
        productService.removeStock(productId, productQuantity);
        invoice.getProducts().add(product);
        return invoice;
    }

    public Invoice removeProduct(Long invoiceId, Long productId, Integer productQuantity) {
        Invoice invoice = findById(invoiceId);
        Product product = productService.findById(productId);
        removeProductQuantity(invoice, productQuantity);
        productService.addStock(productId, productQuantity);
        invoice.getProducts().remove(product);
        return invoice;
    }

    private void addProductQuantity(Invoice invoice, Integer productQuantity) {
        isProductQuantityValid(invoice, productQuantity);
        invoice.setProductQuantity(invoice.getProductQuantity() + productQuantity);
    }

    private void removeProductQuantity(Invoice invoice, Integer productQuantity) {
        isProductQuantityValid(invoice, productQuantity);
        invoice.setProductQuantity(invoice.getProductQuantity() - productQuantity);
    }

    private void isProductQuantityValid(Invoice invoice, Integer productQuantity) {
        if (productQuantity > invoice.getProductQuantity()) {
            throw new IllegalArgumentException("Quantidade de produtos inválida!");
        }
    }

    private long calculateSequentialNumber() {
        return invoiceRepository.count() + 1;
    }

    private static Invoice.Status setStatusOpen() {
        return Invoice.Status.OPEN;
    }

}
