package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.UserDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.UserUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.AlreadyExistsException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.User;
import br.com.viniciusacoelho.invoice_issuance_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(UserDTO userDTO) {
        existsFields(userDTO.email(), userDTO.username(), userDTO.cpf());
        User user = User.builder()
                .name(userDTO.name())
                .email(userDTO.email().toLowerCase().trim())
                .username(userDTO.username().toLowerCase().trim())
                .cpf(userDTO.cpf())
                .cep(userDTO.cep())
                .birthDate(userDTO.birthDate())
                .password(userDTO.password().trim())
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    public List<User> read() {
        hasUsers();
        return userRepository.findAll();
    }

    public User update(Long id, UserUpdateDTO userUpdateDTO) {
        User user = findById(id);
        if (!userUpdateDTO.email().equalsIgnoreCase(user.getEmail())) {
            existsByEmail(userUpdateDTO.email());
        }
        if (!userUpdateDTO.username().equalsIgnoreCase(user.getUsername())) {
            existsByUsername(userUpdateDTO.username());
        }
        if (!userUpdateDTO.cpf().equalsIgnoreCase(user.getCpf())) {
            existsByCpf(userUpdateDTO.cpf());
        }
        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());
        user.setUsername(userUpdateDTO.username());
        user.setCpf(userUpdateDTO.cpf());
        user.setCep(userUpdateDTO.cep());
        return userRepository.save(user);
    }

    public User delete(Long id) {
        hasUser(id);
        userRepository.deleteById(id);
        return null;
    }

    private void existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("E-mail");
        }
    }

    private void existsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("Usuário");
        }
    }

    private void existsByCpf(String cpf) {
        if (userRepository.existsByCpf(cpf)) {
            throw new AlreadyExistsException("CPF");
        }
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário"));
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("E-mail"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuário"));
    }

    public List<User> findByName(String name) {
        return userRepository.findByNameContaining(name)
                .orElseThrow(() -> new NotFoundException("Usuário"));
    }

    private void hasUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário");
        }
    }

    private void hasUsers() {
        if (userRepository.count() == 0) {
            throw new NotFoundException("Usuário");
        }
    }

    private void existsFields(String email, String username, String cpf) {
        existsByEmail(email);
        existsByUsername(username);
        existsByCpf(cpf);
    }

}
