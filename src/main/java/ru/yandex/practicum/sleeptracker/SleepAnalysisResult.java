package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    private String description;
    private Object value;

    public SleepAnalysisResult(String description, Object value) {
        this.description = description;
        this.value = value;
    }

    @Override
    public String toString() {
        return description + ": " + value;
    }
}
