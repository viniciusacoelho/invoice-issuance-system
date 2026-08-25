package br.com.viniciusacoelho.invoice_issuance_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    private long sequentialNumber;

    public enum Status {
        OPEN, CLOSED
    }

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private int productQuantity;

// TODO: Check why it isn't working (it is adding the product_id to the products, not to the invoices).
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
    @ManyToMany
    private final List<Product> products = new ArrayList<>();

}
