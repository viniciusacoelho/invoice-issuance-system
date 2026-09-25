package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Address;
import br.com.viniciusacoelho.invoice_issuance_system.repository.AddressRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ViaCepService viaCepService;

    private Address create(String cep) {
        Address address = viaCepService.findByCep(cep);
        hasAddress(address);
        return addressRepository.save(address);
    }

    @Cacheable(value = "viaCepCache", key = "#cep")
    public Address findByCep(String cep) {
        return addressRepository.findById(cep)
                .orElseGet(() -> create(cep));
    }

    private static void hasAddress(Address address) {
        if (address.getCep() == null) {
            throw new NotFoundException("CEP");
        }
    }

}
