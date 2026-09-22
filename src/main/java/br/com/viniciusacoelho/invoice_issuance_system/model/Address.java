package br.com.viniciusacoelho.invoice_issuance_system.model;

import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @Column(length = 9)
    private String cep;

    private String logradouro;

    private String complemento;

    private String unidade;

    private String bairro;

    private String localidade;

    @Column(length = 2)
    private String uf;

    private String estado;

    private String regiao;

    @Column(length = 7)
    private String ibge;

    private String gia;

    @Column(length = 2)
    private String ddd;

    @Column(length = 4)
    private String siafi;

    public void validateCep(String cep) {
        validateFormat(cep);
//        existsCep(cep, cep.erro);

    }

    public void validateFormat(String cep) {
        if (cep.length() != 8) {
            throw new BadRequestException("CEP");
        }
    }

    public void existsCep(String cep, boolean erro) {
        if (erro == true) {
            throw new NotFoundException("CEP");
        }
    }

}