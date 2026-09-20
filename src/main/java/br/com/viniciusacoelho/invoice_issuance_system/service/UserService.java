package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.config.JWTConfig;
import br.com.viniciusacoelho.invoice_issuance_system.dto.LoginDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.SessionDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.UserDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.UserUpdateDTO;
import br.com.viniciusacoelho.invoice_issuance_system.exception.AlreadyExistsException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.InvalidCredentialsException;
import br.com.viniciusacoelho.invoice_issuance_system.exception.NotFoundException;
import br.com.viniciusacoelho.invoice_issuance_system.model.User;
import br.com.viniciusacoelho.invoice_issuance_system.repository.UserRepository;
import br.com.viniciusacoelho.invoice_issuance_system.security.JWTCreator;
import br.com.viniciusacoelho.invoice_issuance_system.security.JWTObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTConfig jwtConfig;

    public User create(UserDTO userDTO) {
        existsFields(userDTO.email(), userDTO.username(), userDTO.cpf());
        User user = User.builder()
                .name(userDTO.name())
                .email(userDTO.email().toLowerCase().trim())
                .username(userDTO.username().toLowerCase().trim())
                .cpf(userDTO.cpf())
                .cep(userDTO.cep())
                .birthDate(userDTO.birthDate())
                .password(encrypt(userDTO.password().trim()))
                .createdAt(LocalDateTime.now())
                .roles(List.of(User.Role.USER)) // TODO: Change the user roles in other part
//                .roles(List.of(User.Role.ADMIN)) // TODO: Change the user roles in other part
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
//        User user = findByEmail(userUpdateDTO.email());
//        User user = findByUsername(userUpdateDTO.username());
        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());
        user.setUsername(userUpdateDTO.username());
        user.setCpf(userUpdateDTO.cpf());
        user.setCep(userUpdateDTO.cep());
        return userRepository.save(user);
    }

    public User delete(Long id) {
//        User user = findById(id);
//        User user = findByEmail(user.getEmail());
//        User user = findByUsername(user.getUsername());
        hasUser(id);
        userRepository.deleteById(id);
        return null;
    }

    public SessionDTO login(LoginDTO loginDTO) {
        Optional<User> user = findByUsernameLogin(loginDTO.getUsername());
        if (user.isPresent() && isPasswordMatches(loginDTO.getPassword(), user.get().getPassword())) {
            JWTObject jwtObject = JWTObject.builder()
                    .subject(user.get().getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                    .roles(convertRole(user.get().getRoles()))
                    .build();
            return SessionDTO.builder()
                    .login(user.get().getUsername())
                    .token(JWTCreator.create(jwtConfig.getPrefix(), jwtConfig.getKey(), jwtObject))
                    .build();
        }
        throw new InvalidCredentialsException();
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

    public Optional<User> findByUsernameLogin(String username) {
        return userRepository.findByUsername(username);
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

    private String encrypt(String password) {
        return encoder.encode(password);
    }

    private boolean isPasswordMatches(String loginPassword, String userPassword) {
        return encoder.matches(loginPassword, userPassword);
    }

    private List<String> convertRole(List<User.Role> roles) {
        return roles
                .stream()
                .map(Enum::name)
                .toList();
    }

}
