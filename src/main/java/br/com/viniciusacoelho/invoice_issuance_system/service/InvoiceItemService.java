package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import br.com.viniciusacoelho.invoice_issuance_system.model.Product;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class    InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private ProductService productService;

    public List<InvoiceItem> create(InvoiceDTO invoiceDTO) {
// TODO: Try if have a way to do it with stream()
//        productService.hasProducts();
//        invoiceDTO.invoiceItemsDTO().stream()
//                .map(InvoiceItemDTO::productId);
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (int i = 0; i < invoiceDTO.invoiceItemsDTO().size(); i++) {
            int productQuantity = invoiceDTO.invoiceItemsDTO().get(i).productQuantity();
            Long productId = invoiceDTO.invoiceItemsDTO().get(i).productId();
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .productId(productId)
                    .productQuantity(productQuantity)
                    .build();
            addProduct(productId, productQuantity, invoiceItems, invoiceItem);
        }
        return invoiceItemRepository.saveAll(invoiceItems);
    }

    public void addProduct(Long productId, Integer productQuantity, List<InvoiceItem> invoiceItems, InvoiceItem invoiceItem) {
        Product product = productService.findById(productId);
        isProductQuantityValid(productQuantity, product.getStock());
        productService.removeStock(productId, productQuantity);
        invoiceItems.add(invoiceItem);
    }

    private static void isProductQuantityValid(Integer productQuantity, Integer productStock) {
        if (productQuantity > productStock) {
            throw new BadRequestException("Quantidade de produtos");
        }
    }

    public List<InvoiceItem> save(InvoiceDTO invoiceDTO) {
// TODO: Try if have a way to do it with stream()
//        productService.hasProducts();
//        invoiceDTO.invoiceItemsDTO().stream()
//                .map(InvoiceItemDTO::productId);
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (int i = 0; i < invoiceDTO.invoiceItemsDTO().size(); i++) {
            int productQuantity = invoiceDTO.invoiceItemsDTO().get(i).productQuantity();
            Long productId = invoiceDTO.invoiceItemsDTO().get(i).productId();
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .productId(productId)
                    .productQuantity(productQuantity)
                    .build();
            addProduct(productId, productQuantity, invoiceItems, invoiceItem);
        }
        return invoiceItemRepository.saveAll(invoiceItems);
    }

}
