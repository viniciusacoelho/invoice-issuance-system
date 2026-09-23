package br.com.viniciusacoelho.invoice_issuance_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvoiceIssuanceSystemApplication {

    void main() {
        SpringApplication.run(InvoiceIssuanceSystemApplication.class);
    }

}
