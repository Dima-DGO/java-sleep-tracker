package ru.yandex.practicum.sleeptracker;

import java.util.Arrays;
import java.util.List;

public class SleepTrackerApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Укажите путь к файлу с логом сна");
            return;
        }

        String filePath = args[0];
        SleepLogValidator validator = new SleepLogValidator();
        List<SleepingSession> sessions = validator.validateAndParse(filePath);

        List<SleepAnalysisFunction> analysisFunctions = Arrays.asList(
                new TotalSessionsFunction(),
                new MinDurationFunction(),
                new MaxDurationFunction(),
                new AverageDurationFunction(),
                new BadQualitySessionsFunction(),
                new SleeplessNightsFunction(),
                new ChronotypeFunction()
        );

        analysisFunctions.forEach(function -> {
            SleepAnalysisResult result = function.apply(sessions);
            System.out.println(result);
        });
    }
}
