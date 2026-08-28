package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductService productService;

    public Invoice create(Long productId, @Positive Integer productQuantity) {
        productService.hasProducts();
        Product product = productService.findById(productId);
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(Invoice.Status.OPEN)
                .build();
        addProduct(invoice, product, productQuantity);
        productService.removeStock(productId, productQuantity);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public List<Invoice> read() {
        if (isInvoice()) {
            return invoiceRepository.findAll();
        }
        throw new NotFoundException("Notas Fiscais");
    }

    public Invoice update(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice delete(Long id) {
        hasInvoice(id);
        invoiceRepository.deleteById(id);
        return null; //  TODO: Return another thing
    }

    public Invoice issue(Long id) {
        Invoice invoice = findById(id);
        if (isStatusOpen(invoice.getStatus())) {
            setStatusClosed(invoice);
            update(invoice);
            return invoice;
        }
        throw new BadRequestException("Status");
    }

    public Invoice addProduct(Long invoiceId, Long productId, Integer productQuantity) {
        Invoice invoice = findById(invoiceId);
        Product product = productService.findById(productId);
        addProductQuantity(invoice, productQuantity, product.getStock());
        productService.removeStock(productId, productQuantity);
        invoice.getProducts().add(product);
        return invoice;
    }

    public Invoice removeProduct(Long invoiceId, Long productId, Integer productQuantity) {
        Invoice invoice = findById(invoiceId);
        Product product = productService.findById(productId);
        removeProductQuantity(invoice, productQuantity, product.getStock());
        productService.addStock(productId, productQuantity);
        invoice.getProducts().remove(product);
        return invoice;
    }

    private Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nota fiscal"));
    }

    private void hasInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new NotFoundException("Nota fiscal");
        }
    }

    private boolean isInvoice() {
        return invoiceRepository.count() > 0;
    }

    // TODO: Add, for exemple, 2x when adding a product twice, for don't show the same product twice
    private void addProduct(Invoice invoice, Product product, Integer productQuantity) {
        isProductQuantityValid(productQuantity, product.getStock());
        invoice.setProductQuantity(invoice.getProductQuantity() + productQuantity);
        invoice.getProducts().add(product);
    }

    private static void addProductQuantity(Invoice invoice, Integer productQuantity, Integer productStock) {
        isProductQuantityValid(productQuantity, productStock);
        invoice.setProductQuantity(invoice.getProductQuantity() + productQuantity);
    }

    private static void removeProductQuantity(Invoice invoice, Integer productQuantity, Integer productStock) {
        isProductQuantityValid(productQuantity, productStock);
        invoice.setProductQuantity(invoice.getProductQuantity() - productQuantity);
    }

    private static void isProductQuantityValid(Integer productQuantity, Integer productStock) {
        if (productQuantity > productStock) {
            throw new BadRequestException("Quantidade de produtos");
        }
    }

    private static void setStatusClosed(Invoice invoice) {
        invoice.setStatus(Invoice.Status.CLOSED);
    }

    private static boolean isStatusOpen(Invoice.Status status) {
        return status == Invoice.Status.OPEN;
    }

    private long calculateSequentialNumber() {
        return invoiceRepository.count() + 1;
    }

}
