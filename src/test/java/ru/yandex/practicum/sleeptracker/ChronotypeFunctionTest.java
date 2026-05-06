package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChronotypeFunctionTest {

    private final ChronotypeFunction function = new ChronotypeFunction();

    @Test
    void testOwlChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: сова", result.toString());
    }

    @Test
    void testLarkChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 30),
                        LocalDateTime.of(2025, 10, 2, 6, 30), SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: жаворонок", result.toString());
    }

    @Test
    void testDoveChronotype() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 30),
                        LocalDateTime.of(2025, 10, 2, 8, 30), SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: голубь", result.toString());
    }

    @Test
    void testTieResultsInChronotype() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30), SleepQuality.GOOD));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: голубь", result.toString());
    }

    @Test
    void testMultipleSessionsOwlDominant() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 45),
                        LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 30),
                        LocalDateTime.of(2025, 10, 3, 9, 45), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 30),
                        LocalDateTime.of(2025, 10, 4, 8, 0), SleepQuality.NORMAL));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: сова", result.toString());
    }

    @Test
    void testMultipleSessionsLarkDominant() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 15),
                        LocalDateTime.of(2025, 10, 2, 6, 45), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 20, 45),
                        LocalDateTime.of(2025, 10, 3, 6, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 45),
                        LocalDateTime.of(2025, 10, 4, 7, 15), SleepQuality.NORMAL));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: жаворонок", result.toString());
    }

    @Test
    void testAllDoveSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 7, 45), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 45),
                        LocalDateTime.of(2025, 10, 3, 7, 30), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 00),
                        LocalDateTime.of(2025, 10, 4, 8, 15), SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Хронотип пользователя: голубь", result.toString());
    }

    @Test
    void testEmptySessionsList() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals("Хронотип пользователя: голубь", result.toString());
    }
}
