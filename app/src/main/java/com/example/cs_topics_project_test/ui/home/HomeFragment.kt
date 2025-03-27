package com.example.cs_topics_project_test.ui.home

//import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.databinding.FragmentHomeBinding
import kotlin.random.Random
import android.widget.Button
//import android.os.Build.VERSION_CODES.R

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
    //fun onClick(view: android.view.View){}

    fun onClick(view: View) {
        val myRandomInt = Random.nextInt(list.size)
        //val tv : TextView=findViewById(R.id.tv6)
        val t: TextView = view.findViewById(R.id.tv6)
        //t.text = list[myRandomInt].toString()
        t.text = list[myRandomInt]
        //var b: Button = findViewById<Button>(R.id.bt)
        //t.text = list[myRandomInt].toString()
    }


}