package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.dto.InvoiceResponseDTO;
import br.com.viniciusacoelho.invoice_issuance_system.model.Invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("""
            SELECT i.id, i.invoiceStatus, c.name, i.totalPrice, i.totalQuantity, i.issuedAt, i.createdAt, u.name
                FROM Invoice i, Customer c, User u
                WHERE c.id = i.customerId AND u.id = i.userId
                ORDER BY i.id ASC
            """)
    List<InvoiceResponseDTO> findAllInvoices();

}
