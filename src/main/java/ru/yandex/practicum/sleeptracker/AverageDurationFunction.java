package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", 0.0);
        }

        double average = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", average);
    }
}
