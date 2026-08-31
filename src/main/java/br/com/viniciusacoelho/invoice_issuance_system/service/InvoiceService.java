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

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private ProductService productService;

    public Invoice create(InvoiceDTO invoiceDTO) {
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(Invoice.Status.OPEN)
                .invoiceItems(invoiceItemService.create(invoiceDTO))
                .totalPrice(BigDecimal.ZERO)
                .build();
        sumTotalProductQuantity(invoice, invoice.getInvoiceItems());
        sumTotalPrice(invoice, invoice.getInvoiceItems());
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> read() {
        hasInvoices();
        return invoiceRepository.findAll();
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

    public Invoice addProduct(Long invoiceId, InvoiceDTO invoiceDTO) {
        Invoice invoice = findById(invoiceId);
        List<InvoiceItem> invoiceItems = invoiceItemService.create(invoiceDTO);
        sumTotalProductQuantity(invoice, invoiceItems);
        sumTotalPrice(invoice, invoiceItems);
        invoice.getInvoiceItems().addAll(invoiceItems);
        return update(invoice);
    }

    public Invoice removeProduct(Long invoiceId, InvoiceDTO invoiceDTO) {
        Invoice invoice = findById(invoiceId);
        for (int i = 0; i < invoice.getInvoiceItems().size(); i++) {
            Long productId = invoiceDTO.invoiceItemsDTO().get(i).productId();
            int productQuantity = invoiceDTO.invoiceItemsDTO().get(i).productQuantity();
            if (productId.equals(invoice.getInvoiceItems().get(i).getProductId())) {
                subtractTotalProductQuantity(invoice, productQuantity);
                subtractTotalPrice(invoice, productId, productQuantity);
                invoiceItemService.remove(productId, productQuantity, invoice.getInvoiceItems(), invoice.getInvoiceItems().get(i));
            }
        }
        return update(invoice);
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

    private void hasInvoices() {
        if (invoiceRepository.count() == 0) {
            throw new NotFoundException("Notas fiscais");
        }
    }

    private void sumTotalProductQuantity(Invoice invoice, List<InvoiceItem> invoiceItems) {
        int productQuantity = calculateProductQuantity(invoiceItems);
        invoice.setTotalProductQuantity(invoice.getTotalProductQuantity() + productQuantity);
    }

    private void subtractTotalProductQuantity(Invoice invoice, int productQuantity) {
        invoice.setTotalProductQuantity(invoice.getTotalProductQuantity() - productQuantity);
    }

    private void sumTotalPrice(Invoice invoice, List<InvoiceItem> invoiceItems) {
        for (InvoiceItem invoiceItem : invoiceItems) {
            Product product = productService.findById(invoiceItem.getProductId());
            invoice.setTotalPrice(calculateSumTotalPrice(invoice.getTotalPrice(), product.getPrice(), invoiceItem.getProductQuantity()));
        }
    }

    private void subtractTotalPrice(Invoice invoice, Long productId, int productQuantity) {
            Product product = productService.findById(productId);
            invoice.setTotalPrice(calculateSubtractTotalPrice(invoice.getTotalPrice(), product.getPrice(), productQuantity));
    }

    private static int calculateProductQuantity(List<InvoiceItem> invoiceItems) {
        return invoiceItems.stream()
                .mapToInt(InvoiceItem::getProductQuantity)
                .sum();
    }

    private static BigDecimal calculateSumTotalPrice(BigDecimal totalPrice, BigDecimal price, int quantity) {
        return totalPrice
                .add(price)
                .multiply(new BigDecimal(quantity));
    }

    private static BigDecimal calculateSubtractTotalPrice(BigDecimal totalPrice, BigDecimal price, int quantity) {
        return totalPrice
                .subtract(price)
                .multiply(new BigDecimal(quantity));
    }

    private long calculateSequentialNumber() {
        return invoiceRepository.count() + 1;
    }

    private static void setStatusClosed(Invoice invoice) {
        invoice.setStatus(Invoice.Status.CLOSED);
    }

    private static boolean isStatusOpen(Invoice.Status status) {
        return status == Invoice.Status.OPEN;
    }

}
