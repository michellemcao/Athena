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
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import android.graphics.Color
import com.example.cs_topics_project_test.function.Date


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var dater : Date? = null

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
        TaskManager.init()

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    private var tasks = TaskManager.tasksDueToday+TaskManager.tasksDueLater+ TaskManager.tasksPastDue

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
    private val list: List<String> = listOf(
        "Why is so great about Switzerland?", "What's the best thing about Switzerland?","Why did the pony need a drink of water?", "What do you call a pig who knows how to use a knife?", "Which vegetable has the best kung fu?", "What did the Zero say to the Eight?", "What do you call a factory that makes okay products?", "How do you organize a space party?", "Where do pirates buy their hooks?", "What did the Buffalo say to his little boy when he left the house?", "What did the janitor say when he jumped out of the closet?", "How can you tell if a pig is hot?", "Why is a guilty panda never caught?", "What did the ocean say to the beach?", "Why are mountains so funny?", "What did the boy say to his fingers?", "What did one wall say to the other?", "Why couldn't the bicycle stand up by itself?", "What sound does a witch's vehicle make?", "What do you call a rude cow?", "How do you cook a crocodile?", "What is a ghost's favorite dessert?", "Why didn’t the skeleton go to homecoming?", "Why are Vampires bad at art?", "What's the problem with twin witches?", "Why don't mummies take time off?", "How do you know when a zombie likes someone?", "What kind of horse does a ghost ride?", "What do you get when you cross a vampire and a snowman?", "What happened to the wicked witch at school?", "Did you hear about the red and blue ships that collided?", "When does a joke become a dad joke?", "When does Friday come before Thursday?", "What's the best air to breathe if you want to be rich?", "What do you call a bear with no teeth?", "What's more amazing than a talking dog?", "Why does a chicken coop only have two doors?", "What do you call it when a cow grows facial hair?", "Why do nurses like red crayons?", "What kind of shoes do frogs wear?", "What do gingerbread men use when they break their legs?", "Where do gingerbread men sleep at night?", "What do you call a snowman with a six pack?", "What did the snowman say to the aggressive carrot?", "Why did the Scarecrow receive such a large holiday bonus?", "What do you get when you cross a snowman with a vampire?", "What do you call a reindeer ghost?", "What do Santa's elves learn in school?", "What kind of money do elves use?", "Why did Rudolph the Red Nosed Reindeer have to go to Summer school?", "What is corn's favorite holiday?", "What would the band AC/DC have been called if all their songs were about world history?", "What do you call anxious dinosaurs?", "Why do crabs never give to charity?", "If most puns make you feel numb how do math puns make you feel?", "What kind of vegetable never bowls a strike?", "Why are paleontologists so annoyed?", "What will an octopus be charged with if they hold up a bank?", "What do you call a mouse that swears?", "What is a tree's favorite dating App?", "Why couldn't the computer take it's hat off?", "What's the easiest building to lift?", "What do you call a retired miner?", "What did the dentist name his boat?", "What type of luggage do vultures bring on airplanes?", "How much does an influencer weigh?", "What did the scientist say to her Valentine?", "What do you call a nervous javelin thrower?", "How did the owl manage his bad breath?", "How did the drummer sell his sofa?", "How do you get an astronaut's baby to sleep?", "What do they upgrade you to when you upload your one thousandth photo to Instagram?", "Why did the chicken cross the playground?", "What did the cupcake say to the icing?", "Why did the sailboat start smoking?", "What's the best time of the year to play on a trampoline?", "What's the best way to save your dad jokes?", "What do you call fifty-two pieces of bread?", "How many storm troopers does it take to change a lightbulb?", "What word can you make shorter by adding two letters?", "What do parents feed an infant if they want them to grow up to be a race-car driver?", "Why did the DJ ride the roller coaster?", "What do you call a perfume that doesn't smell like anything?", "What do you call fake potatoes?", "What did the big flower say to the little flower?", "What did the cactus wear to his job interview?", "What do you get when you cross a fish with an elephant?", "Why did the electric car feel discriminated against?", "How do robots eat pizza?", "What's the best way to overcome a fear of escalators?", "How do you find a cheetah in the dark?", "Why do Java developers wear glasses?", "Why did everyone want to get into the eyeglass convention?", "What do you call a teenager that refuses to grow up?", "Why did the river have trouble remembering things?"
    )
    private val alist: List<String> = listOf(
        "I don’t know, but the flag is a big plus.", "I don’t know, but the flag is a big plus.", "Because he was a little horse.", "A pork chop.", "Brocc-lee.", "Nice belt.", "A satisfactory.", "You planet.", "At the second-hand store.", "Bison.", "Supplies!", "It’s bacon.", "Because it always bears false witness.", "Nothing, it just waved.", "Because they’re hill areas.", "I’m counting on you.", "I’ll meet you at the corner.", "It was two-tired.", "Broom broom!", "Beef jerky.", "In a croc-pot.", "I scream.", "Because he had no body to go with.", "They can only draw blood.", "You never know which witch is which.", "They’re afraid to unwind.", "They go straight for the heart.", "A nightmare.", "Frostbite.", "She was ex-spelled.", "Both crews were marooned.", "When it becomes apparent.", "In the dictionary.", "Millionaire.", "A gummy bear.", "A spelling bee.", "Because if it had four, it would be a chicken sedan.", "A moo-stache.", "Because they often have to draw blood.", "Open toad sandals.", "Candy canes.", "In cookie sheets.", "An abdominal snowman.", "Get out of my face!", "Because he was outstanding in his field.", "Frostbite.", "Cari-boo.", "The elf-abet.", "Jingle bills.", "He went down in history.", "New Ears Day.", "B.C./A.D.", "Nervous Rex.", "Because they’re shellfish.", "Number.", "A spare-agus.", "Because their jobs are always in ruins.", "Armed robbery.", "A cursive mouse.", "Timber.", "Because it had a bad case of CAPS LOCK.", "A light house.", "Doug.", "The Tooth Ferry.", "Carrion.", "An Instagram.", "I've got chemistry with you.", "Shaky spear.", "Owli-tic.", "On cymbal.", "You rocket.", "Instagold.", "To get to the other slide.", "I’d be muffin without you.", "Because it couldn’t knot quit.", "Spring time.", "In a pun vault.", "A deck of carbs.", "None. They miss every shot.", "Short.", "Formula One.", "Because he wanted to drop the bass.", "Non-scents.", "Imitators.", "Hi, bud.", "A spike tie.", "Swimming trunks.", "Because it was always being charged.", "With megabytes.", "Take steps.", "Spot it.", "Because they don’t C#.", "Because it was a spectacle.", "A kid-ult.", "Because it kept going in one ear and out the otter."
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<PieChart>(R.id.piechart)
        configChartView(chart)

        val chart2 = view.findViewById<PieChart>(R.id.piechart2)
        configChartView2(chart2)

        val t: TextView = view.findViewById(R.id.tv6)
        val myRandomInt = Random.nextInt(list.size)
        if(dater!=TaskManager.todayDate){
            t.text = list[myRandomInt]
            dater=TaskManager.todayDate
        }

        val b : Button=view.findViewById(R.id.bt)

        b.setOnClickListener {
            if(t.text==list[myRandomInt]){
                t.text=alist[myRandomInt]
            }
            else{
                t.text=list[myRandomInt]
            }
        }

        var todo=""
        val tasks = TaskManager.tasksDueToday
        for(i in 0..tasks.size-1){
            if(!tasks[i].isTaskCompleted()){
                todo=todo+" •"+tasks[i].getTaskName()+"\n"
            }
        }
        val unfinished : TextView=view.findViewById(R.id.unfinished)
        unfinished.text=todo





    }

    private fun configChartView2(chart: PieChart) {
        val total = (TaskManager.tasksDueLater.size + TaskManager.tasksPastDue.size + TaskManager.tasksDueToday.size)
        if(total==0){
            chart.clearChart()
            return
        }
        val countlater = (TaskManager.tasksDueLater.size*100f)/total
        val countpast = (TaskManager.tasksPastDue.size*100f)/total
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
        val total = (TaskManager.tasksDueLater.size+ TaskManager.tasksPastDue.size+ TaskManager.tasksDueToday.size+TaskManager.tasksCompleted.size)
        if(total==0){
            chart.clearChart()
            return
        }
        val countdone = (TaskManager.tasksCompleted.size*100f)/total
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