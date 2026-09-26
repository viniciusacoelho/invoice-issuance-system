package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.CustomerUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.enums.Role;
import br.com.viniciusacoelho.invoice_issuance_system.exception.AlreadyExistsException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.BadRequestException;
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

    @Autowired
    private AddressService addressService;

    public Customer create(CustomerDTO customerDTO) {
        existsByEmail(customerDTO.email());
        existsByCpf(customerDTO.cpf());
        existsByCnpj(customerDTO.cnpj());
        Customer customer = Customer.builder()
                .name(customerDTO.name())
                .email(customerDTO.email())
                .phone(validatePhone(customerDTO.phone()))
                .cpf(customerDTO.cpf())
                .cnpj(customerDTO.cnpj())
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        customer.setAddress(addressService.findByCep(customerDTO.cep()));
        return customerRepository.save(customer);
    }

    public List<Customer> read() {
        hasCustomers();
        return customerRepository.findAll();
    }

    public Customer update(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = findById(id);
        if (!customer.getEmail().equalsIgnoreCase(customerUpdateDTO.email())) {
            existsByEmail(customerUpdateDTO.email());
        }
        if (!customer.getCpf().equalsIgnoreCase(customerUpdateDTO.cpf())) {
            existsByCpf(customerUpdateDTO.cpf());
        }
        if (!customer.getCnpj().equals(customerUpdateDTO.cnpj())) {
            existsByCnpj(customerUpdateDTO.cnpj());
        }
        customer.setName(customerUpdateDTO.name());
        customer.setEmail(customerUpdateDTO.email());
        customer.setPhone(validatePhone(customerUpdateDTO.phone()));
        customer.setCpf(customerUpdateDTO.cpf());
        customer.setCnpj(customerUpdateDTO.cnpj());
        customer.setAddress(addressService.findByCep(customerUpdateDTO.cep()));
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

    private void existsByEmail(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("E-mail");
        }
    }

    private void existsByCpf(String cpf) {
        if (customerRepository.existsByCpf(cpf)) {
            throw new AlreadyExistsException("CPF");
        }
    }

    private void existsByCnpj(String cnpj) {
        if (customerRepository.existsByCnpj(cnpj)) {
            throw new AlreadyExistsException("CNPJ");
        }
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

    private String validatePhone(String phone) {
        if (phone.matches("^[+]\\d{2} \\(\\d{2}\\) \\d{1} \\d{4}-\\d{4}$")) {
            return phone;
        }
        throw new BadRequestException("Telefone");
    }

}
