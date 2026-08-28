package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

}
