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
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Pie
import com.example.cs_topics_project_test.task.Task
import com.example.cs_topics_project_test.task.TaskManager.tasks

/*import com.anychart.AnyChart.pie
import com.anychart.Pie
import com.anychart.ValueDataEntry
*/
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
        /*
        binding.bt.setOnClickListener {
            // Handle the click event here
            onClick()
        }*/



    }

    private var chart: AnyChartView? = null
    fun onCreate(view: AnyChartView, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_home)
        val chart: AnyChartView=view.findViewById(R.id.any_chart_view)
        configChartView()

    }

    private fun configChartView() {

        var checked =0
        var notchecked =0
        for(i in 0..tasks.size-1){
            if(tasks[i].isTaskCompleted()){
                checked++
            }
            else{
                notchecked++
            }
        }
        val work = listOf(checked, notchecked)
        val done = listOf("done", "not done")
        val pie: Pie =AnyChart.pie()
        val dataPieChart: MutableList<DataEntry> = mutableListOf()
        for(index in work.indices){
            dataPieChart.add(ValueDataEntry(done.elementAt(index),work.elementAt(index)))
        }
        pie.data(dataPieChart)
        pie.title("Finished work")
        chart!!.setChart(pie)
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
    }


}