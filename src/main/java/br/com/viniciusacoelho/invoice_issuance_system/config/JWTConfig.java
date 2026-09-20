package br.com.viniciusacoelho.invoice_issuance_system.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.config")
@Data
public class JWTConfig {

    private String prefix;

    private String key;

    private Long expiration;

}
