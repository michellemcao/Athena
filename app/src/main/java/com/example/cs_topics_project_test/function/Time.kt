package com.example.cs_topics_project_test.function

class Time(hour: Int, min: Int/*, private val isPM: Boolean*/) : Comparable<Time> {
    private val hour: Int
    private val min: Int

    init {
        if (hour > 23) throw IllegalArgumentException("invalid hour as input")
        else this.hour = hour
        if (min > 60) throw IllegalArgumentException("invalid minute as input")
        else this.min = min
    }

    // returns hour in 0-23 format
    fun getHour(): Int {
        return this.hour // should I return in 0-23 or 1-12?
    }

    fun getMin(): Int {
        return this.min
    }

    fun isPM(): Boolean {
        /*return this.isPM*/
        return this.hour >= 12
    }

    /*override fun compareTo(other: Time): Int {
        if (this.hour == other.hour && this.min == other.min && this.second == other.second) return 0;

        if (this.hour > other.hour) return 1
        else if (this.hour == other.hour) {
            if (this.min > other.min) return 1
            else if (this.min == other.min) {
                if (this.second > other.second) return 1
            }
        }
        return -1;
    }*/

    /*override fun compareTo(other: Time): Int {
        if (this.hour == other.hour && this.min == other.min) return 0;
        if (this.hour > other.hour) return 1
        else if (this.hour == other.hour) {
            if (this.min > other.min) return 1
        }
        return -1;
    }*/
    override fun compareTo(other: Time): Int {
        if (this == other) return 0 // calls the equals method
        /*if (this.isPM && !other.isPM) return 1 // if this is PM and other is AM
        if (!this.isPM && other.isPM) return -1// if this is AM and other is PM*/
        // if this.isPM == other.isPM
        if (this.hour == other.hour) { // if hours are equal to each other
            if (this.min > other.min) return 1
        }
        // if hour == 12, it's automatically the smallest
        /*if (this.hour == 12) return -1
        if (other.hour == 12) return 1*/
        if (this.hour > other.hour) return 1
        return -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Time) return false // checks if they are of same class
        return /*this.isPM == other.isPM &&*/ this.hour == other.hour && this.min == other.min
    }

    override fun toString(): String {
        val addZero : String = if (this.min < 10) { "0" } else ""
        val isPM : String = if (this.hour >= 12) { "PM" } else "AM"
        var hour = this.hour
        if (hour > 12) hour -= 12
        else if (hour == 0) hour += 12
        return hour.toString() + ":" + addZero + this.min + " " +  isPM
    }
}