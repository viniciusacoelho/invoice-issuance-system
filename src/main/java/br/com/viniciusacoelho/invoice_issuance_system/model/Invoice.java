package br.com.viniciusacoelho.invoice_issuance_system.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long sequentialNumbering;

    public enum Status {
        OPEN, CLOSE
    }

//    @Enumerated()
    private Status status;

// TODO: Check why it isn't working (it is adding the product_id to the products, not to the invoices).
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
    @OneToMany
    private List<Product> products;

}
