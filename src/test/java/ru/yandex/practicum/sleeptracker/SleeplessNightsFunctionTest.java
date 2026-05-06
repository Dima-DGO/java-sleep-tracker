package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsFunctionTest {

    private final SleeplessNightsFunction function = new SleeplessNightsFunction();


    @Test
    void testNoSleeplessNights_SingleNightSession() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 0", result.toString());
    }

    @Test
    void testMultipleSleeplessNights() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 5, 23, 0),
                        LocalDateTime.of(2025, 10, 6, 8, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 3", result.toString());
    }


    @Test
    void testSleeplessNightsAtEndOfPeriod() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 0", result.toString());
    }

    @Test
    void testSessionSpanningMultipleNights() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 5, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 0", result.toString());
    }

    @Test
    void testEmptySessionsList() {
        List<SleepingSession> sessions = List.of();

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 0", result.toString());
    }

    @Test
    void testConsecutiveSessionsNoGaps() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 0", result.toString());
    }

    @Test
    void testDaytimeSessionsOnly() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 12, 0),
                        LocalDateTime.of(2025, 10, 1, 14, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 13, 0),
                        LocalDateTime.of(2025, 10, 2, 15, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 2", result.toString());
    }

    @Test
    void testCrossMonthBoundary() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 30, 23, 0),
                        LocalDateTime.of(2025, 10, 31, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 11, 2, 22, 0),
                        LocalDateTime.of(2025, 11, 3, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 2", result.toString());
    }

    @Test
    void testSingleSessionSameDay() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 16, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Количество бессонных ночей: 1", result.toString());
    }
}
