package br.com.viniciusacoelho.invoice_issuance_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/swagger-ui/index.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/logout").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.POST, "/invoices").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/invoices").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/invoices/{id}/issue").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/invoices/{id}/add", "/invoices/{id}/remove").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/invoices/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/products", "/products/filter/name={name}", "/products/filter/category={category}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{username}", "/users/find/{name}").permitAll()
                        // TODO: The user can only update/delete using their own account.
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
