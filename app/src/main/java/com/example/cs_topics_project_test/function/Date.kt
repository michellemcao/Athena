package com.example.cs_topics_project_test.function

class Date(year: Int, month: Int, date: Int) {
    private val date: Int
    private val month: Int
    private val year: Int

    init {
        this.year = year
        if (month > 12) throw IllegalArgumentException("invalid month as input")
        else this.month = month
        if (date > 31) throw IllegalArgumentException("invalid date as input")
        else this.date = date
    }

    fun getYear(): Int {
        return year
    }

    fun getMonth(): Int {
        return month
    }

    fun getDate(): Int {
        return date
    }

    override fun toString(): String {
        return "$month/$date/$year"
    }
}