package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SleepLogValidatorTest {

    @TempDir
    Path tempDir;
    private final SleepLogValidator validator = new SleepLogValidator();

    @Test
    void testValidFileParsing() throws IOException {
        Path testFile = tempDir.resolve("test_sleep_log.txt");
        String validContent = "01.10.25 22:15;02.10.25 08:00;GOOD\n" +
                "02.10.25 23:00;03.10.25 08:00;NORMAL";
        Files.writeString(testFile, validContent);

        SleepLogValidator validator = new SleepLogValidator();
        List<SleepingSession> sessions = validator.validateAndParse(testFile.toString());

        assertNotNull(sessions);
        assertEquals(2, sessions.size());

        SleepingSession session1 = sessions.get(0);
        assertEquals(LocalDateTime.parse("01.10.25 22:15",
                        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                session1.getStartTime());
        assertEquals(LocalDateTime.parse("02.10.25 08:00",
                        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                session1.getEndTime());
        assertEquals(SleepQuality.GOOD, session1.getQuality());
    }

    @Test
    void testEmptyLinesIgnored() throws IOException {
        Path testFile = tempDir.resolve("test_sleep_log.txt");
        String contentWithEmpty = "\n\n01.10.25 22:15;02.10.25 08:00;GOOD\n  \n";
        Files.writeString(testFile, contentWithEmpty);

        SleepLogValidator validator = new SleepLogValidator();
        List<SleepingSession> sessions = validator.validateAndParse(testFile.toString());

        assertEquals(1, sessions.size());

        SleepingSession session = sessions.get(0);
        assertEquals(LocalDateTime.parse("01.10.25 22:15",
                        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                session.getStartTime());
        assertEquals(LocalDateTime.parse("02.10.25 08:00",
                        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                session.getEndTime());
        assertEquals(SleepQuality.GOOD, session.getQuality());
    }

    @Test
    void testInvalidSegmentCount() {
        String invalidLine = "01.10.25 22:15;02.10.25 08:00";
        ValidationResult result = validator.validateSingleLineForTesting(invalidLine, null);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Неверное количество сегментов в сессии: " + invalidLine));
    }

    @Test
    void testInvalidDateFormat() {
        String invalidDate = "01/10/25 22:15;02.10.25 08:00;GOOD";
        ValidationResult result = validator.validateSingleLineForTesting(invalidDate, null);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e ->
                e.contains("Неверный формат даты времени начала сессии")));
    }

    @Test
    void testStartTimeAfterEndTime() {
        String invalidOrder = "02.10.25 08:00;01.10.25 22:15;GOOD";
        ValidationResult result = validator.validateSingleLineForTesting(invalidOrder, null);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Неверное начало и конец сессии: " + invalidOrder));
    }

    @Test
    void testSessionOverlap() {
        LocalDateTime prevEnd = LocalDateTime.of(2025, 10, 2, 8, 0);
        String overlapping = "02.10.25 07:00;02.10.25 09:00;GOOD";
        ValidationResult result = validator.validateSingleLineForTesting(overlapping, prevEnd);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Сессия пересекается с предыдущей: " + overlapping));
    }

    @Test
    void testLongSession() {
        String longSession = "01.10.25 00:00;02.10.25 01:00;GOOD"; // 25 часов
        ValidationResult result = validator.validateSingleLineForTesting(longSession, null);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Сессия слишком длинная: " + longSession));
    }

    @Test
    void testInvalidSleepQuality() {
        String badQuality = "01.10.25 22:15;02.10.25 08:00;UNKNOWN";
        ValidationResult result = validator.validateSingleLineForTesting(badQuality, null);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Неверное значение качества сна: UNKNOWN"));
    }
}

