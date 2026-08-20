package br.com.viniciusacoelho.invoice_issuance_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// TODO: Check for a way to fix the issue that occurs when updating the product, where the system consistently indicates that null values cannot be accepted.
//    @Column(nullable = false)
//    private long code = this.id * 1000;
    private Long code;

//    @Column(length = 50, nullable = false)
    @Column(length = 50)
    private String name;

//    @Column(length = 1000, nullable = false)
    @Column(length = 1000)
    private String description;

    //    @Column(nullable = false)
    private Long quantity;

    //    @Column(nullable = false)
    private BigDecimal balance;

}
