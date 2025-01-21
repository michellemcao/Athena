package com.example.cs_topics_project_test.function

class Time(hour: Int, min: Int, private val isPM: Boolean) : Comparable<Time> {
    private val hour: Int
    private val min: Int

    init {
        if (hour > 24) throw IllegalArgumentException("invalid hour as input")
        else this.hour = hour
        if (min > 60) throw IllegalArgumentException("invalid minute as input")
        else this.min = min
    }

    fun getHour(): Int {
        return this.hour
    }

    fun getMin(): Int {
        return this.min
    }

    fun isPM(): Boolean {
        return this.isPM
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

    override fun compareTo(other: Time): Int {
        if (this.hour == other.hour && this.min == other.min) return 0;
        if (this.hour > other.hour) return 1
        else if (this.hour == other.hour) {
            if (this.min > other.min) return 1
        }
        return -1;
    }


    override fun toString(): String {
        val addZero : String
        if (this.min < 10) {addZero = "0"}
        else addZero = ""
        return this.hour.toString() + ":" + addZero + this.min + " " +  if (isPM) "PM" else "AM"
    }
}