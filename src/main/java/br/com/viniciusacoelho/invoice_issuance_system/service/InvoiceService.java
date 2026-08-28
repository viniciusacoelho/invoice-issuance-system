package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceItemService invoiceItemService;

    public Invoice create(InvoiceDTO invoiceDTO) {
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(Invoice.Status.OPEN)
                .invoiceItems(invoiceItemService.create(invoiceDTO))
                .build();
        return invoiceRepository.save(invoice);
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

// TODO: Add multiple products to he update
//    public Invoice addProduct(Long invoiceId, Long productId, Integer productQuantity) {
//        Invoice invoice = findById(invoiceId);
//        Product product = productService.findById(productId);
//        addProductQuantity(invoice, productQuantity, product.getStock());
//        productService.removeStock(productId, productQuantity);
//        invoice.getProducts().add(product);
//        return invoice;
//    }
//
// TODO: Remove multiple products to the update
//    public Invoice removeProduct(Long invoiceId, Long productId, Integer productQuantity) {
//        Invoice invoice = findById(invoiceId);
//        Product product = productService.findById(productId);
//        removeProductQuantity(invoice, productQuantity, product.getStock());
//        productService.addStock(productId, productQuantity);
//        invoice.getProducts().remove(product);
//        return invoice;
//    }

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
