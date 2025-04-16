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
import com.example.cs_topics_project_test.task.TaskManager.tasksDueLater
import com.example.cs_topics_project_test.task.TaskManager.tasksDueToday
import com.example.cs_topics_project_test.task.TaskManager.tasksPastDue
import kotlin.random.Random
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import android.graphics.Color
import com.example.cs_topics_project_test.task.TaskManager.tasksCompleted


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

    private var tasks = tasksDueToday+tasksDueLater+ tasksPastDue

    //private var chart: PieChart? = null
    /*fun onCreate(pieChart: PieChart,savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_home)
        chart = pieChart.findViewById(R.id.piechart)
        configChartView()

        //chart2=view.findViewById(R.id.any_chart_view2)
        //configChartView2()
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //
        //    }
    }*/






    /*private var chart2: AnyChartView? = null

    private fun configChartView2() {

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
        val column: Cartesian =AnyChart.column()
        val dataColumnChart: MutableList<DataEntry> = mutableListOf()
        for(index in work.indices){
            dataColumnChart.add(ValueDataEntry(done.elementAt(index),work.elementAt(index)))
        }
        val series =column.column(dataColumnChart)
        series.normal().fill("#E294AF")
        column.data(dataColumnChart)
        column.xAxis(0).title("Work")
        column.yAxis(0).title("Amount")

        chart2!!.setChart(column)

    }*/



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

        val chart = view.findViewById<PieChart>(R.id.piechart)
        configChartView(chart)

        val chart2 = view.findViewById<PieChart>(R.id.piechart2)
        configChartView2(chart2)


        val b : Button=view.findViewById(R.id.bt)
        val t: TextView = view.findViewById(R.id.tv6)
        b.setOnClickListener {
            val myRandomInt = Random.nextInt(list.size)
            t.text = list[myRandomInt]
        }

        var todo=""
        val tasks = TaskManager.tasksDueToday
        for(i in 0..tasks.size-1){
            if(!tasks[i].isTaskCompleted()){
                todo=todo+" -"+tasks[i].getTaskName()+"\n"
            }
        }
        val unfinished : TextView=view.findViewById(R.id.unfinished)
        unfinished.text=todo





    }

    private fun configChartView2(chart: PieChart) {
        val total = (tasksDueLater.size+ tasksPastDue.size+ tasksDueToday.size)
        if(total==0){
            chart.clearChart()
            return
        }
        val countlater = (tasksDueLater.size*100f)/total
        val countpast = (tasksPastDue.size*100f)/total
        val counttoday = 100-countlater-countpast
        chart.clearChart()

        chart.addPieSlice(
            PieModel("Tasks Due Later",countlater,
                Color.parseColor("#E8A5B0")
            )
        )

        chart.addPieSlice(
            PieModel("Tasks Past Due",countpast,
                Color.parseColor("#E65069")
            )
        )

        chart.addPieSlice(
            PieModel("Tasks Due Today", counttoday,
                Color.parseColor("#E28291")
            )
        )
        chart.startAnimation()


    }

    private fun configChartView(chart: PieChart) {
        val total = (tasksDueLater.size+ tasksPastDue.size+ tasksDueToday.size+tasksCompleted.size)
        if(total==0){
            chart.clearChart()
            return
        }
        val countdone = (tasksCompleted.size*100f)/total
        val countnotdone = 100-countdone
        chart.clearChart()

        chart.addPieSlice(
            PieModel("done",countdone,
                Color.parseColor("#B8E6A4")
            )
        )




        chart.addPieSlice(
            PieModel("not done", countnotdone,
                Color.parseColor("#CF475D")
            )
        )
        chart.startAnimation()


    }

}