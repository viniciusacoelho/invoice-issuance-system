package br.com.viniciusacoelho.invoice_issuance_system.security;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class JWTObject {

    private String subject;

    private Date issuedAt;

    private Date expiration;

    private List<String> roles;

}
