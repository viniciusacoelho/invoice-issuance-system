package br.com.viniciusacoelho.invoice_issuance_system.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

}
