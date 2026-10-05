package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.InvoiceRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.response.InvoiceResponseDTO;
import br.com.viniciusacoelho.invoice_issuance_system.enums.InvoiceStatus;
import br.com.viniciusacoelho.invoice_issuance_system.exception.InvoiceCannotBeIssuedException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.OperationAlreadyInProgressException;
import br.com.viniciusacoelho.invoice_issuance_system.model.IdempotencyKey;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemService invoiceItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private IdempotencyKeyService idempotencyKeyService;

    @Transactional
    public Invoice create(InvoiceRequestDTO invoiceRequestDTO, String idempotencyKey) {
        Optional<IdempotencyKey> key = idempotencyKeyService.findById(idempotencyKey);
        if (key.isPresent()) {
            if (IdempotencyKeyService.isStatusProcessing(key.get().getStatus())) {
                throw new OperationAlreadyInProgressException();
            } else if (IdempotencyKeyService.isStatusCompleted(key.get().getStatus())) {
                return findById(key.get().getInvoiceId());
            }
        }
        IdempotencyKey keyReserved = idempotencyKeyService.reserve(idempotencyKey);
        Invoice invoice = Invoice.builder()
                .sequentialNumber(calculateSequentialNumber())
                .status(InvoiceStatus.OPEN)
                .customerId(invoiceRequestDTO.customerId())
                .totalPrice(BigDecimal.ZERO)
                .userId(invoiceRequestDTO.userId())
                .createdAt(LocalDateTime.now())
                .build();
        invoice.setInvoiceItems(invoiceItemService.create(invoice.getSequentialNumber(), invoiceRequestDTO));
        sumTotalProductQuantity(invoice, invoice.getInvoiceItems());
        sumTotalPrice(invoice, invoice.getInvoiceItems());
        Invoice newInvoice = invoiceRepository.save(invoice);
        idempotencyKeyService.update(keyReserved, invoice.getId());
        return newInvoice;
    }

    public List<InvoiceResponseDTO> read() {
        hasInvoices();
        List<InvoiceResponseDTO> invoices = new ArrayList<>();
        for (InvoiceResponseDTO invoice : invoiceRepository.findAllInvoices()) {
            invoice.setInvoiceItems(invoiceItemService.findAllByInvoiceId(invoice.getId()));
            invoices.add(invoice);
        }
        return invoices;
    }

    public Invoice update(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice delete(Long id) {
        hasInvoice(id);
        invoiceRepository.deleteById(id);
        return null; //  TODO: Return another thing
    }

    // TODO: CSV issuing
    public InvoiceResponseDTO issue(Long id) {
//    public InvoiceResponseDTO issue(Long invoiceId, Long userId) {
        Invoice invoice = findById(id);
        if (isStatusOpen(invoice.getStatus())) {
            setStatusClosed(invoice);
            invoice.setIssuedAt(LocalDateTime.now());
            update(invoice);
            return readById(id);
        }
        throw new InvoiceCannotBeIssuedException("A nota fiscal deve estar em aberto para ser emitida.");
    }

    public Invoice addProduct(Long invoiceId, InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = findById(invoiceId);
        List<InvoiceItem> invoiceItems = invoiceItemService.create(invoiceId, invoiceRequestDTO);
        sumTotalProductQuantity(invoice, invoiceItems);
        sumTotalPrice(invoice, invoiceItems);
        invoice.getInvoiceItems().addAll(invoiceItems);
        return update(invoice);
    }

    public Invoice removeProduct(Long invoiceId, InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = findById(invoiceId);
        for (int i = 0; i < invoice.getInvoiceItems().size(); i++) {
            Long productId = invoiceRequestDTO.invoiceItemsDTO().get(i).productId();
            int productQuantity = invoiceRequestDTO.invoiceItemsDTO().get(i).productQuantity();
            if (productId.equals(invoice.getInvoiceItems().get(i).getProductId())) {
                subtractTotalProductQuantity(invoice, productQuantity);
                subtractTotalPrice(invoice, productId, productQuantity);
                invoiceItemService.remove(productId, productQuantity, invoice.getInvoiceItems(), invoice.getInvoiceItems().get(i));
            }
        }
        return update(invoice);
    }

    private InvoiceResponseDTO readById(Long id) {
        InvoiceResponseDTO invoice = invoiceRepository.findInvoiceById(id);
        invoice.setInvoiceItems(invoiceItemService.findAllByInvoiceId(invoice.getId()));
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

    private void hasInvoices() {
        if (invoiceRepository.count() == 0) {
            throw new NotFoundException("Notas fiscais");
        }
    }

    private void sumTotalProductQuantity(Invoice invoice, List<InvoiceItem> invoiceItems) {
        int productQuantity = calculateProductQuantity(invoiceItems);
        invoice.setTotalQuantity(invoice.getTotalQuantity() + productQuantity);
    }

    private void subtractTotalProductQuantity(Invoice invoice, int productQuantity) {
        invoice.setTotalQuantity(invoice.getTotalQuantity() - productQuantity);
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
        invoice.setStatus(InvoiceStatus.CLOSED);
    }

    private static boolean isStatusOpen(InvoiceStatus invoiceStatus) {
        return invoiceStatus == InvoiceStatus.OPEN;
    }

}
