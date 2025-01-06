package com.example.cs_topics_project_test.function_java;

import androidx.annotation.NonNull;

public class Time {
    private int hour = 0;
    private final int min;
    private final boolean isAm;
    public Time(int hour, int min, boolean isAm) {
        this.min = min % 60;
        if (min > 60) {
            hour += min / 60;
        }
        if (hour > 24) {
            throw new IllegalArgumentException("Invalid Time entered");
        }
        this.hour = hour % 12;
        if (hour >= 12) {
            this.isAm = false;
        } else {
            this.isAm = true;
        }
    }

    public int getHour() {
        return this.hour;
    }

    public int getMin() {
        return this.min;
    }

    public boolean isAm() {
        return this.isAm;
    }

    public boolean isPm() {
        return !this.isAm;
    }

    @NonNull
    @Override
    public String toString() {
        return this.hour + ":" + this.min;
    }
}
