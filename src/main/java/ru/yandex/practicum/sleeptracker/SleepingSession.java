package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SleepQuality quality;

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, SleepQuality quality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = quality;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setQuality(SleepQuality quality) {
        this.quality = quality;
    }

    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
}

