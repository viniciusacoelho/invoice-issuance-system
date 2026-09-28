package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.dto.response.InvoiceItemResponseDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("""
            SELECT p.id, p.name, p.price, ii.productQuantity
            	FROM Product p, InvoiceItem ii
            	WHERE p.id = ii.productId AND ii.invoiceId = :invoiceId
            """)
    List<InvoiceItemResponseDTO> findAllByInvoiceId(Long invoiceId);

}
