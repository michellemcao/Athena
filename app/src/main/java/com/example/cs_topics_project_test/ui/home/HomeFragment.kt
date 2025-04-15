package com.example.cs_topics_project_test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.FragmentHomeBinding
import com.example.cs_topics_project_test.task.TaskManager
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val list: List<String> = listOf("What do you call a factory that makes okay products? ",
        "How do you organize a space party?",
        "Where do pirates buy their hooks?",
        "What did the janitor say when he jumped out of the closet?",
        "What did the Buffalo say to his little boy when he left the house?",
        "Why is so great about Switzerland?",
        "Why did the pony need a drink of water?",
        "What do you call a pig who knows how to use a knife?")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val b : Button=view.findViewById(R.id.bt)

        val t: TextView = view.findViewById(R.id.tv6)
        b.setOnClickListener {
            val myRandomInt = Random.nextInt(list.size)
            t.text = list[myRandomInt]
        }
        var todo=""
        var tasks = TaskManager.tasksDueToday
        for(i in 0..tasks.size-1){
            if(!tasks[i].isTaskCompleted()){
                todo=todo+" -"+tasks[i].getTaskName()+"\n"
            }
        }
        val unfinished : TextView=view.findViewById(R.id.unfinished)
        unfinished.text=todo

    }

}