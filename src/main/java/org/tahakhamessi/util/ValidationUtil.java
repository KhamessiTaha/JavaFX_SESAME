package org.tahakhamessi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ValidationUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isPositivePrice(double price) {
        return price > 0;
    }

    public static boolean isEndDateValid(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
            return end.isAfter(start) || end.isEqual(start);
        } catch (Exception e) {
            return false;
        }
    }

    public static int calculateDays(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
            return (int) ChronoUnit.DAYS.between(start, end) + 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMAT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRequiredFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        return phone == null || phone.isEmpty() || phone.matches("\\d+");
    }

    public static boolean isPermisExpired(String expiration) {
        try {
            LocalDate exp = LocalDate.parse(expiration, DATE_FORMAT);
            return exp.isBefore(LocalDate.now());
        } catch (Exception e) {
            return true;
        }
    }
}

