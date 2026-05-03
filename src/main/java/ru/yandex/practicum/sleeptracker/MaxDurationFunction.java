package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.OptionalLong;

public class MaxDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        OptionalLong max = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max();

        long duration = max.orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", duration);
    }
}
