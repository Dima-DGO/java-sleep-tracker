package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class SleepLogValidator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public List<SleepingSession> validateAndParse(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<SleepingSession> sessions = new ArrayList<>();
            LocalDateTime previousEndTime = null;

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                ValidationResult result = validateLine(line, previousEndTime);
                if (!result.isValid()) {
                    result.getErrors().forEach(System.out::println);
                    continue;
                }

                SleepingSession session = parseLine(line);
                sessions.add(session);
                previousEndTime = session.getEndTime();
            }

            return sessions;
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private ValidationResult validateLine(String line, LocalDateTime previousEndTime) {
        ValidationResult result = new ValidationResult();
        String[] parts = line.split(";");

        if (parts.length != 3) {
            result.addError("Неверное количество сегментов в сессии: " + line);
            return result;
        }

        LocalDateTime startTime = parseDateTime(parts[0], result, "Неверный формат даты времени начала сессии");
        LocalDateTime endTime = parseDateTime(parts[1], result, "Неверный формат даты времени окончания сессии");

        if (startTime == null || endTime == null) return result;

        if (!startTime.isBefore(endTime)) {
            result.addError("Неверное начало и конец сессии: " + line);
            return result;
        }

        if (previousEndTime != null && (startTime.isBefore(previousEndTime) || startTime.equals(previousEndTime))) {
            result.addError("Сессия пересекается с предыдущей: " + line);
            return result;
        }

        if (java.time.Duration.between(startTime, endTime).toHours() >= 24) {
            result.addError("Сессия слишком длинная: " + line);
            return result;
        }

        try {
            SleepQuality.valueOf(parts[2]);
        } catch (IllegalArgumentException e) {
            result.addError("Неверное значение качества сна: " + parts[2]);
            return result;
        }

        return result;
    }

    private LocalDateTime parseDateTime(String dateTimeStr, ValidationResult result, String errorMessage) {
        try {
            return LocalDateTime.parse(dateTimeStr, FORMATTER);
        } catch (DateTimeParseException e) {
            result.addError(errorMessage + ": " + dateTimeStr);
            return null;
        }
    }

    private SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime startTime = LocalDateTime.parse(parts[0], FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(parts[1], FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2]);
        return new SleepingSession(startTime, endTime, quality);
    }

    public ValidationResult validateSingleLineForTesting(String line, LocalDateTime previousEndTime) {
        return validateLine(line, previousEndTime);
    }
}
