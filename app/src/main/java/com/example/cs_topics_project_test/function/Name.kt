package com.example.cs_topics_project_test.function

class Name (private var firstName: String, private var lastName: String) : Comparable<Name> {
    fun changeFirstName(name: String) {
        this.firstName = name
    }

    fun changeLastName(name: String) {
        this.lastName = name
    }

    fun changeName(firstName: String, lastName: String) {
        changeFirstName(firstName)
        changeLastName(lastName)
    }


    // I believe if run with sort() this will return list sorted by ascending first name
    // do we need this??
    @Override
    override fun compareTo(other: Name): Int {
        return this.firstName.compareTo(other.firstName)
    }

    fun orderFirstNameAscending (): Comparator<Name> {
        return FirstNameAtoZ()
    }
    private class FirstNameAtoZ() : Comparator<Name> {
        override fun compare(p1: Name, p2: Name): Int {
            return p1.firstName.compareTo(p2.firstName)
        }
    }

    fun orderFirstNameDescending (): Comparator<Name> {
        return FirstNameZtoA()
    }
    private class FirstNameZtoA() : Comparator<Name> {
        override fun compare(p1: Name, p2: Name): Int {
            return p2.firstName.compareTo(p1.firstName)
        }
    }

    fun orderLastNameAscending (): Comparator<Name> {
        return LastNameAtoZ()
    }
    private class LastNameAtoZ() : Comparator<Name> {
        override fun compare(p1: Name, p2: Name): Int {
            return p1.lastName.compareTo(p2.lastName)
        }
    }

    fun orderLastNameDescending (): Comparator<Name> {
        return LastNameZtoA()
    }
    private class LastNameZtoA() : Comparator<Name> {
        override fun compare(p1: Name, p2: Name): Int {
            return p2.lastName.compareTo(p1.lastName)
        }
    }
}