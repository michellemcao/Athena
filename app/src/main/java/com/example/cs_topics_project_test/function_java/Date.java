package com.example.cs_topics_project_test.function_java;

import androidx.annotation.NonNull;

public final class Date {
    private final int date;
    private final int month;
    private final int year;

    public Date(int date, int year, int month) {
        if (date > 31) {
            int mul = date / 31;
            month += mul;
            date = date % 31;
        }
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public int getDate() {
        return date;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }

    @NonNull
    @Override
    public String toString() {
        return month + "/" + date + "/" + year;
    }
}
