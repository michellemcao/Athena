package com.example.cs_topics_project_test.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment

class ChatFragment : Fragment() {

    companion object {
        private const val ARG_PERSON = "arg_person"

        fun newInstance(person: Person): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putParcelable(ARG_PERSON, person)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the Person object from arguments
        arguments?.let {
            person = it.getParcelable(ARG_PERSON) ?: throw IllegalArgumentException("Person must be provided")
        }
    }
}

