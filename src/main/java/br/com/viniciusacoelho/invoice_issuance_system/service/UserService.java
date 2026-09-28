package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.dto.request.UserRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.request.UserUpdateRequestDTO;
import br.com.viniciusacoelho.invoice_issuance_system.enums.Role;
import br.com.viniciusacoelho.invoice_issuance_system.exception.AlreadyExistsException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.User;
import br.com.viniciusacoelho.invoice_issuance_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User create(UserRequestDTO userRequestDTO) {
        existsByEmail(userRequestDTO.email());
        existsByUsername(userRequestDTO.username());
        User user = User.builder()
                .name(userRequestDTO.name())
                .email(userRequestDTO.email().toLowerCase().trim())
                .username(userRequestDTO.username().toLowerCase().trim())
                .birthDate(userRequestDTO.birthDate())
                .password(encrypt(userRequestDTO.password().trim()))
                .createdAt(LocalDateTime.now())
                .roles(List.of(Role.USER)) // TODO: Change the user roles in other part
//                .roles(List.of(Role.ADMIN, Role.USER)) // TODO: Change the user roles in other part
                .build();
        return userRepository.save(user);
    }

    public List<User> read() {
        hasUsers();
        return userRepository.findAll();
    }

    public User update(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = findById(id);
        if (!userUpdateRequestDTO.email().equalsIgnoreCase(user.getEmail())) {
            existsByEmail(userUpdateRequestDTO.email());
        }
        if (!userUpdateRequestDTO.username().equalsIgnoreCase(user.getUsername())) {
            existsByUsername(userUpdateRequestDTO.username());
        }
        user.setName(userUpdateRequestDTO.name());
        user.setEmail(userUpdateRequestDTO.email());
        user.setUsername(userUpdateRequestDTO.username());
        return userRepository.save(user);
    }

    public User delete(Long id) {
        hasUser(id);
        userRepository.deleteById(id);
        return null;
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário"));
    }

    public List<User> findByName(String name) {
        return userRepository.findByNameContaining(name)
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

    private void hasUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário");
        }
    }

    private void hasUsers() {
        if (userRepository.count() == 0) {
            throw new NotFoundException("Usuários");
        }
    }

    private String encrypt(String password) {
        return encoder.encode(password);
    }

}
