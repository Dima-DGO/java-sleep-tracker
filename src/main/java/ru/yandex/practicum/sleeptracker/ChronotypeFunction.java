package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    public enum Chronotype {
        OWL,
        LARK,
        DOVE
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        // Обработка пустого списка сессий
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "голубь");
        }

        List<Chronotype> nightTypes = sessions.stream()
                .filter(this::isNightSession)
                .map(this::determineNightType)
                .collect(Collectors.toList());

        if (nightTypes.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "голубь");
        }

        Map<Chronotype, Long> typeCounts = nightTypes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Chronotype dominantType = determineDominantChronotype(typeCounts);

        String chronotypeName = convertToDisplayName(dominantType);
        return new SleepAnalysisResult("Хронотип пользователя", chronotypeName);
    }


    private boolean isNightSession(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();

        return startTime.isBefore(LocalTime.of(6, 0)) ||
                endTime.isAfter(LocalTime.of(0, 0));
    }


    private Chronotype determineNightType(SleepingSession session) {
        LocalTime bedTime = session.getStartTime().toLocalTime();
        LocalTime wakeTime = session.getEndTime().toLocalTime();

        if (bedTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        } else if (bedTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        } else {
            return Chronotype.DOVE;
        }
    }

    private Chronotype determineDominantChronotype(Map<Chronotype, Long> typeCounts) {
        Long maxCount = typeCounts.values().stream().max(Long::compareTo).orElse(0L);

        List<Chronotype> maxTypes = typeCounts.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxCount))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (maxTypes.size() > 1) {
            return Chronotype.DOVE;
        } else {
            return maxTypes.isEmpty() ? Chronotype.DOVE : maxTypes.get(0);
        }
    }

    private String convertToDisplayName(Chronotype type) {
        switch (type) {
            case OWL:
                return "сова";
            case LARK:
                return "жаворонок";
            default:
                return "голубь";
        }
    }
}

