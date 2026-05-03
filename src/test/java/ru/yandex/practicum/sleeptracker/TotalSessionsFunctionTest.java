package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TotalSessionsFunctionTest {

    private final TotalSessionsFunction function = new TotalSessionsFunction();

    @Test
    void testTotalSessionsCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Общее количество сессий сна: 2", result.toString());
    }

    @Test
    void testEmptySessionsList() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals("Общее количество сессий сна: 0", result.toString());
    }
}
