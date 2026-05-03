package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.OptionalLong;

public class MinDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        OptionalLong min = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .min();

        long duration = min.orElse(0);
        return new SleepAnalysisResult("Минимальная продолжительность сессии (мин)", duration);
    }
}
