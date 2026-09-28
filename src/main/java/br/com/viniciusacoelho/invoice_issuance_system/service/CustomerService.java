package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.CustomerRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.request.CustomerUpdateRequestDTO;
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

    public Customer create(CustomerRequestDTO customerRequestDTO) {
        existsByEmail(customerRequestDTO.email());
        existsByCpf(customerRequestDTO.cpf());
        existsByCnpj(customerRequestDTO.cnpj());
        Customer customer = Customer.builder()
                .name(customerRequestDTO.name())
                .email(customerRequestDTO.email())
                .phone(validatePhone(customerRequestDTO.phone()))
                .cpf(customerRequestDTO.cpf())
                .cnpj(customerRequestDTO.cnpj())
                .createdAt(LocalDateTime.now())
                .build();
        customer.setAddress(addressService.findByCep(customerRequestDTO.cep()));
        return customerRepository.save(customer);
    }

    public List<Customer> read() {
        hasCustomers();
        return customerRepository.findAll();
    }

    public Customer update(Long id, CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        Customer customer = findById(id);
        if (!customer.getEmail().equalsIgnoreCase(customerUpdateRequestDTO.email())) {
            existsByEmail(customerUpdateRequestDTO.email());
        }
        if (!customer.getCpf().equalsIgnoreCase(customerUpdateRequestDTO.cpf())) {
            existsByCpf(customerUpdateRequestDTO.cpf());
        }
        if (!customer.getCnpj().equals(customerUpdateRequestDTO.cnpj())) {
            existsByCnpj(customerUpdateRequestDTO.cnpj());
        }
        customer.setName(customerUpdateRequestDTO.name());
        customer.setEmail(customerUpdateRequestDTO.email());
        customer.setPhone(validatePhone(customerUpdateRequestDTO.phone()));
        customer.setCpf(customerUpdateRequestDTO.cpf());
        customer.setCnpj(customerUpdateRequestDTO.cnpj());
        customer.setAddress(addressService.findByCep(customerUpdateRequestDTO.cep()));
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
