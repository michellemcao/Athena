package com.example.cs_topics_project_test.function

class Date(private val year: Int, month: Int, date: Int) : Comparable<Date> {
    private val date: Int
    private val month: Int

    init {
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

    override fun compareTo(other: Date): Int {
        if (this == other) return 0;

        if (this.year > other.year) return 1
        else if (this.year == other.year) {
            if (this.month > other.month) return 1
            else if (this.month == other.month) {
                if (this.date > other.date) return 1
            }
        }
        return -1;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Date) return false // checks if they are of same class
        return this.year == other.year && this.month == other.month && this.date == other.date
    }

    override fun toString(): String {
        // if (date < 10) return "$month/0$date/$year"
        return "$month/$date/$year"
    }
}