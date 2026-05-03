package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BadQualitySessionsFunctionTest {

    private final BadQualitySessionsFunction function = new BadQualitySessionsFunction();

    @Test
    void testBadQualityCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.BAD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество сессий с плохим качеством сна: 2", result.toString());
    }

    @Test
    void testNoBadQualitySessions() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество сессий с плохим качеством сна: 0", result.toString());
    }
}
