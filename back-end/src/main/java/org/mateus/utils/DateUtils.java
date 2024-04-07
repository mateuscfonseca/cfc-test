package org.mateus.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {

    public static ZoneId defaultZone() {
        return ZoneId.of("America/Sao_Paulo");
    }

    public static LocalDateTime dataHoraAtual() {
        return ZonedDateTime.now(DateUtils.defaultZone()).toLocalDateTime();
    }

    public static LocalDate dataAtual() {
        return LocalDate.now(DateUtils.defaultZone());
    }

}
