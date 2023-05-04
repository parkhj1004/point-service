package org.point.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String LAST_DATE_TIME = " 23:59:59";

    public static LocalDateTime format(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.parse(getCurrentDateTime(), FORMATTER);
    }

    public static String addDay(int days) {
        return LocalDateTime.now().plusDays(days).format(FORMATTER_DATE) + LAST_DATE_TIME;
    }

    public static LocalDateTime getLocalDateTimeAddDay(int days) {
        return LocalDateTime.parse(addDay(days), FORMATTER);
    }
}
