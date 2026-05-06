package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MinDurationFunctionTest {

    private final MinDurationFunction function = new MinDurationFunction();

    @Test
    void testMinDuration() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.NORMAL));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Минимальная продолжительность сессии (мин): 50", result.toString());
    }

    @Test
    void testSingleSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Минимальная продолжительность сессии (мин): 585", result.toString());
    }

    @Test
    void testEmptySessions() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals("Минимальная продолжительность сессии (мин): 0", result.toString());
    }
}

