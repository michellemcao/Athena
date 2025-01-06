package com.example.cs_topics_project_test.function

import kotlin.random.Random

class Profile (firstName: String, lastName: String, email: String, start: DateAndTime) {
    private var name: Name // is the extra class really necessary?
    private val email: String // Use val for a variable whose value never changes
    private var token: Int
    private val start: DateAndTime

    /*
     * firebase handles authentication part, pwd etc
     * this is for our reference with the chat feature
     * for example, the token will be a unique randomly generated number that will be used to connect your account to another persons account
     * email will be used to connect your account to its firebase side
     * name is just the display name
     * start is the start date and time; do we really need it?
     */
    init {
        this.name = Name(firstName, lastName)
        this.email = email
        this.token = Random.nextInt(1000, 10000)
        // if token in unique token list stored in firebase, then regenerate
        this.start = start
    }

    fun changefirstName(name: String) {
        this.name.changeFirstName(name)
    }

    fun changelastName(name: String) {
        this.name.changeLastName(name)
    }

    fun changeName(firstName: String, lastName: String) {
        this.name.changeName(firstName, lastName)
    }

    fun getEmail(): String {
        return this.email
    }

    /*
     * comparator based of firstName and lastName (from Name class)
     * comparator based on startDateAndTime (should I implement one for DateAndTime?)
     * arrayList of the tokens, stored to general firebase
     */

}