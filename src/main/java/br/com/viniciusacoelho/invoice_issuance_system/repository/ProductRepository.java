package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.model.Product;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    List<Product> findByNameContaining(String name);

    List<Product> findByCategory(String category);

    List<Product> findByNameContainingOrderByPriceAsc(String name);

    List<Product> findByNameContainingOrderByPriceDesc(String name);

    List<Product> findByNameContainingOrderByStockAsc(String name);

    List<Product> findByNameContainingOrderByStockDesc(String name);

    List<Product> findByCategoryContainingOrderByPriceAsc(String category);

    List<Product> findByCategoryContainingOrderByPriceDesc(String category);

    List<Product> findByCategoryContainingOrderByStockAsc(String category);

    List<Product> findByCategoryContainingOrderByStockDesc(String category);

}
