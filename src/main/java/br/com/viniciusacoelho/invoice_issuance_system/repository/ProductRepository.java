package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name);

    List<Product> findByCategory(String category);

}
