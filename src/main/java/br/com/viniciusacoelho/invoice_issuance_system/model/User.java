package br.com.viniciusacoelho.invoice_issuance_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 14, unique = true, nullable = false)
    private String cpf;

    @Column(length = 9, nullable = false)
    private String cep;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Role {
        ADMIN, USER
    }

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
