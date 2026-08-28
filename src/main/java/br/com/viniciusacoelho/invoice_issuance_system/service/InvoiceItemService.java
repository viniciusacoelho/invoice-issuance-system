package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private ProductService productService;

    public List<InvoiceItem> create(InvoiceDTO invoiceDTO) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (int i = 0; i < invoiceDTO.invoiceItemsDTO().size(); i++) {
            Long productId = invoiceDTO.invoiceItemsDTO().get(i).productId();
            int productQuantity = invoiceDTO.invoiceItemsDTO().get(i).productQuantity();
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .productId(productId)
                    .productQuantity(productQuantity)
                    .build();
            add(productId, productQuantity, invoiceItems, invoiceItem);
        }
        return invoiceItemRepository.saveAll(invoiceItems);
    }

    public void add(Long productId, Integer productQuantity, List<InvoiceItem> invoiceItems, InvoiceItem invoiceItem) {
        Product product = productService.findById(productId);
        isProductQuantityValid(productQuantity, product.getStock());
        productService.removeStock(productId, productQuantity);
        invoiceItems.add(invoiceItem);
    }

    public void remove(Long productId, Integer productQuantity, List<InvoiceItem> invoiceItems, InvoiceItem invoiceItem) {
        productService.addStock(productId, productQuantity);
        invoiceItems.remove(invoiceItem);
        delete(productId);
    }

    private void delete(Long id) {
        invoiceItemRepository.deleteById(id);
    }

    private static void isProductQuantityValid(Integer productQuantity, Integer productStock) {
        if (productQuantity > productStock) {
            throw new BadRequestException("Quantidade do produto");
        }
    }

}
