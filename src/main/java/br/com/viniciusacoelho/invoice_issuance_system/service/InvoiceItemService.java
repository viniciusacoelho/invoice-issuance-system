package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.InvoiceRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.response.InvoiceItemResponseDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import br.com.viniciusacoelho.invoice_issuance_system.repository.InvoiceItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceItemService {

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public List<InvoiceItem> create(Long invoiceId, InvoiceRequestDTO invoiceRequestDTO) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (int i = 0; i < invoiceRequestDTO.invoiceItemsDTO().size(); i++) {
            Long productId = invoiceRequestDTO.invoiceItemsDTO().get(i).productId();
            int productQuantity = invoiceRequestDTO.invoiceItemsDTO().get(i).productQuantity();
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .invoiceId(invoiceId)
                    .productId(productId)
                    .productQuantity(productQuantity)
                    .build();
            add(productId, productQuantity, invoiceItems, invoiceItem);
        }
        return invoiceItemRepository.saveAll(invoiceItems);
    }

    public List<InvoiceItemResponseDTO> findAllByInvoiceId(Long id) {
        return invoiceItemRepository.findAllByInvoiceId(id);
    }

    public void add(Long productId, Integer productQuantity, List<InvoiceItem> invoiceItems, InvoiceItem invoiceItem) {
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

}
