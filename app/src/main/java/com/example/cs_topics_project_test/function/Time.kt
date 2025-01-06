package com.example.cs_topics_project_test.function

class Time(hour: Int, min: Int, second: Int) {
    private val hour: Int
    private val min: Int
    private val second: Int

    init {
        if (hour > 24) throw IllegalArgumentException("invalid hour as input")
        else this.hour = hour
        if (min > 60) throw IllegalArgumentException("invalid minute as input")
        else this.min = min
        if (second > 60) throw IllegalArgumentException("invalid minute as input")
        else this.second = second
    }

    fun getHour(): Int {
        return this.hour
    }

    fun getMin(): Int {
        return this.min
    }

    fun getSecond(): Int {
        return this.second
    }

    override fun toString(): String {
        return this.hour.toString() + ":" + this.min + ":" + this.second
    }
}