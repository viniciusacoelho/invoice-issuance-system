package br.com.viniciusacoelho.invoice_issuance_system.repository;

import br.com.viniciusacoelho.invoice_issuance_system.model.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
