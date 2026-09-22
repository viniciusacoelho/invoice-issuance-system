package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.enums.Role;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.Customer;
import br.com.viniciusacoelho.invoice_issuance_system.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer create(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .name(customerDTO.name())
                .email(customerDTO.email())
                .cpf(customerDTO.cpf())
                .cnpj(customerDTO.cnpj())
                .cep(customerDTO.cep())
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        return customerRepository.save(customer);
    }

    public List<Customer> read() {
        hasCustomers();
        return customerRepository.findAll();
    }

    public Customer update(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = findById(id);
        customer.setName(customerUpdateDTO.name());
        customer.setEmail(customerUpdateDTO.email());
        customer.setCpf(customerUpdateDTO.cpf());
        customer.setCnpj(customerUpdateDTO.cnpj());
        customer.setCep(customerUpdateDTO.cep());
        return customerRepository.save(customer);
    }

    public Customer delete(Long id) {
        hasCustomer(id);
        customerRepository.deleteById(id);
        return null;
    }

    private Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente"));
    }

    private void hasCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Cliente");
        }
    }

    private void hasCustomers() {
        if (customerRepository.count() != 0) {
            throw new NotFoundException("Clientes");
        }
    }

}
