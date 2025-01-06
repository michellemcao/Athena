package com.example.cs_topics_project_test.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.ui.chat.ChatFragment

class ChatFragment : Fragment() {

    companion object {
        private const val ARG_PERSON = "person"

        fun newInstance(person: Person): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putSerializable(ARG_PERSON, person)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var person: Person? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var peopleAdapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            person = it.getSerializable(ARG_PERSON) as Person
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.peopleRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Create some mock data for people
        val peopleList = listOf(
            Person("Alice", 1),
            Person("Bob", 2),
            Person("Charlie", 3)
        )

        // Set up the adapter
        peopleAdapter = PersonAdapter(peopleList) { person ->
            // Handle item click and pass the selected person to a new fragment
            // For example, navigate to a new chat screen for the selected person
            // You can use a fragment transaction to replace the current fragment
            // Example: show the chat screen for the selected person
            // You might want to pass `person` to a new `ChatDetailsFragment`
        }

        recyclerView.adapter = peopleAdapter

        return view
    }
}
