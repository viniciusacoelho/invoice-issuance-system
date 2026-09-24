package br.com.viniciusacoelho.invoice_issuance_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class InvoiceIssuanceSystemApplication {

    void main() {
        SpringApplication.run(InvoiceIssuanceSystemApplication.class);
    }

}
