package br.com.viniciusacoelho.invoice_issuance_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("Invoice Issuance System API")
                .description("REST API for managing products and issuing invoices.")
                .contact(contact())
                .license(license())
                .version("1.0.0")
                .summary("API for the Invoice Issuance System.");
    }

    private Contact contact() {
        return new Contact()
                .name("Vinícius Araújo Coêlho")
                .url("https://www.linkedin.com/in/viniciusacoelho/")
                .email("viniciusaccoelho123@gmail.com");
    }

    private License license() {
        return new License()
                .name("MIT License")
                .url("https://github.com/viniciusacoelho/invoice-issuance-system/blob/main/LICENSE");
    }

}
