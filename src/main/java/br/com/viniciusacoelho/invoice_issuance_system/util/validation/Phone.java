package br.com.viniciusacoelho.invoice_issuance_system.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "^\\+55 \\(\\d{2}\\) 9 \\d{4}-\\d{4}$")
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Target({ FIELD, PARAMETER, METHOD, ANNOTATION_TYPE, CONSTRUCTOR, TYPE_USE })
@Retention(RUNTIME)
public @interface Phone {

    String message() default "Telefone inválido!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
