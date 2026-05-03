package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AverageDurationFunctionTest {

    private final AverageDurationFunction function = new AverageDurationFunction();

    @Test
    void testAverageDuration() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.NORMAL));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Средняя продолжительность сессии (мин): 391.6666666666667", result.toString());
    }

    @Test
    void testEmptySessions() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals("Средняя продолжительность сессии (мин): 0.0", result.toString());
    }
}

