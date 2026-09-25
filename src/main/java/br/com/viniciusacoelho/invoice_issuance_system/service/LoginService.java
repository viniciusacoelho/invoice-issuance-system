package br.com.viniciusacoelho.invoice_issuance_system.service;

import br.com.viniciusacoelho.invoice_issuance_system.config.JWTConfig;
import br.com.viniciusacoelho.invoice_issuance_system.dto.LoginDTO;
import br.com.viniciusacoelho.invoice_issuance_system.dto.SessionDTO;
import br.com.viniciusacoelho.invoice_issuance_system.enums.Role;
import br.com.viniciusacoelho.invoice_issuance_system.exception.InvalidCredentialsException;
import br.com.viniciusacoelho.invoice_issuance_system.model.User;
import br.com.viniciusacoelho.invoice_issuance_system.security.JWTCreator;
import br.com.viniciusacoelho.invoice_issuance_system.security.JWTObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTConfig jwtConfig;

    public SessionDTO login(LoginDTO loginDTO) {
        Optional<User> user = Optional.ofNullable(userService.findByUsername(loginDTO.username()));
        if (user.isPresent() && isPasswordMatches(loginDTO.password(), user.get().getPassword())) {
            return session(user.get());
        }
        throw new InvalidCredentialsException();
    }

    private boolean isPasswordMatches(String loginPassword, String userPassword) {
        return encoder.matches(loginPassword, userPassword);
    }

    private SessionDTO session(User user) {
        return SessionDTO.builder()
                .login(user.getUsername())
                .token(JWTCreator.create(jwtConfig.getPrefix(), jwtConfig.getKey(), jwtObject(user)))
                .build();
    }

    private JWTObject jwtObject(User user) {
        return JWTObject.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .roles(convertRole(user.getRoles()))
                .build();
    }

    private static List<String> convertRole(List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }

}
