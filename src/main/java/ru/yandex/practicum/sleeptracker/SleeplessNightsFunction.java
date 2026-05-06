package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SleeplessNightsFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDateTime firstStart = sessions.getFirst().getStartTime();
        LocalDateTime lastEnd = sessions.getLast().getEndTime();

        LocalDate effectiveStart = getEffectiveStartDate(firstStart);
        LocalDate endDate = lastEnd.toLocalDate();

        if (effectiveStart.isAfter(endDate)) {
            effectiveStart = endDate;
        }

        int sleeplessCount = 0;
        LocalDate currentDate = effectiveStart;

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime nightStart = currentDate.atTime(0, 0);
            LocalDateTime nightEnd = currentDate.atTime(6, 0);

            boolean isNightCovered = sessions.stream()
                    .anyMatch(session -> isNightInSession(session, nightStart, nightEnd));

            if (!isNightCovered) {
                sleeplessCount++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessCount);
    }

    private LocalDate getEffectiveStartDate(LocalDateTime firstSessionStart) {
        if (firstSessionStart.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            return firstSessionStart.toLocalDate().plusDays(1);
        } else {
            return firstSessionStart.toLocalDate();
        }
    }

    private boolean isNightInSession(SleepingSession session, LocalDateTime nightStart,
                                     LocalDateTime nightEnd) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalDateTime sessionEnd = session.getEndTime();

        boolean startsBeforeNightEnd = sessionStart.isBefore(nightEnd) || sessionStart.equals(nightEnd);
        boolean endsAfterNightStart = sessionEnd.isAfter(nightStart) || sessionEnd.equals(nightStart);

        return startsBeforeNightEnd && endsAfterNightStart;
    }
}

