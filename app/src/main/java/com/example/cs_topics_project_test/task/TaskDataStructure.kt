package com.example.cs_topics_project_test.task

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.DateCompleted
import com.example.cs_topics_project_test.function.Time
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.NavigableMap
import java.util.TreeMap

object TaskDataStructure {
    /* Psychology behind data structure:
     * People generally tend to schedule multiple tasks around the same time in their task manager.
     * This helps them receive notifications etc at the same time.
     * As such a linked list for the tasks (value field) is more beneficial than unique key for every new task added
     */

    // private val taskMap = TreeMap<DateAndTime, MutableList<Task>>()
    // private val taskMap = TreeMap<DateAndTime, TaskDetail>()
    private val taskMap = TreeMap<DateAndTime, TaskNode?>()
    private val completedMap = TreeMap<DateCompleted, TaskNode>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser

    private class TaskNode (
        val task: TaskDetail,
        var nextTask: TaskNode?
    )

    // TaskMap functionality and retrieval code

    // adding task to taskMap
    fun addTask(key: DateAndTime, value: TaskDetail) : Boolean {
        /*if (keyExists(key)) {
            if (!taskMap[key]?.contains(value)!!) taskMap[key]?.add(value)
            Log.d("addTask", "duplicate task identified. task not re-added")
        }*/
        /*taskMap[key] = mutableListOf(value) // creates new list and inserts task*/
        /*taskMap[key] = value // creates new list and inserts task*/
        if (keyExists(key)) {
            var current = taskMap[key]
            while (current != null) {
                if (current.task == value) return false // indicating to TaskActivity that adding failed cause duplicate exists
                current = current.nextTask
            }
            val place = taskMap[key]
            taskMap[key] = TaskNode(value, place)
        } else {
            taskMap[key] = TaskNode(value, null)
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeTask(key: DateAndTime, value: TaskDetail): Boolean {
        if (!keyExists(key)) return false

        var current = taskMap[key]
        var prev: TaskNode? = null

        while (current != null) {
            if (current.task == value) {
                // Removes the specific TaskDetail node
                if (prev == null) {
                    taskMap[key] = current.nextTask
                } else {
                    prev.nextTask = current.nextTask
                }
                // Remove the key if no tasks left
                if (taskMap[key] == null) {
                    taskMap.remove(key)
                }
                // removes task from firestore
                deleteTask(value.getTaskName(), value.getTaskDescription(), key.getUnixTime())
                return true
            }
            prev = current
            current = current.nextTask
        }
        return false // task not found
    }


    // checking is key exists in taskMap
    private fun keyExists(key: DateAndTime) : Boolean {
        return taskMap.containsKey(key)
    }

    fun rangeListFrom(lowerBound: DateAndTime, lowerInclusive: Boolean) : MutableList<Task> {
        if (taskMap.isEmpty() || taskMap.lastKey() <= lowerBound) {
            return mutableListOf<Task>() /*rangeList(lowerBound, lowerInclusive,
                DateAndTime(Date(2026, 1, 1), Time(0, 0, false)),
                false)*/
        }
        return rangeList(lowerBound, lowerInclusive, taskMap.lastKey(), true)
    }

    fun rangeListTo(upperBound: DateAndTime, upperInclusive: Boolean) : MutableList<Task> {
        if (taskMap.isEmpty() || taskMap.firstKey() >= upperBound) {
            return mutableListOf<Task>() /*rangeList(
                DateAndTime(Date(2025, 1, 1), Time(0, 0, false)),
                true, upperBound, upperInclusive)*/
        }
        return rangeList(taskMap.firstKey(), true, upperBound, upperInclusive)
    }

    // the tasks in a specific day
    fun rangeDateTasks(date : Date) : MutableList<Task> {
        return rangeList(
            DateAndTime(date, Time(0, 0)), true,
            DateAndTime(date, Time(23, 59)), true)
    }

    // taskMap helped functions
    private fun rangeMap(lowerBound : DateAndTime,
                         lowerInclusive : Boolean,
                         upperBound : DateAndTime,
                         upperInclusive : Boolean) : NavigableMap<DateAndTime, TaskNode?> {
        return taskMap.subMap(lowerBound, lowerInclusive, upperBound, upperInclusive)
    }

    // returns list of task in specific range as Task Object
    fun rangeList(lowerBound : DateAndTime,
                  lowerInclusive : Boolean,
                  upperBound : DateAndTime,
                  upperInclusive : Boolean) : MutableList<Task> {
        val rangeMap = rangeMap(lowerBound, lowerInclusive, upperBound, upperInclusive)
        val taskList = mutableListOf<Task>()
        for ((key, value) in rangeMap) {
            var current : TaskNode? = value
            while (current != null) {
                val task = Task(
                    current.task.getTaskName(), current.task.getTaskDescription(), key.getDate(), key.getTime())
                taskList.add(task)
                current = current.nextTask
            }
            /*val task = Task(value.getTaskName(), value.getTaskDescription(), key.getDate(), key.getTime())
            taskList.add(task)*/
        }
        return taskList
    }

    // completedMap functionality and processing

    // complete task and process it
    fun processCompletedTask(key: DateAndTime, value: TaskDetail) {
        if (!keyExists(key)) return

        var current = taskMap[key] // TaskNode
        var prev : TaskNode? = null

        while (current != null) {
            if (current.task == value) {
                if (prev == null) { // if the first node is what we are looking for
                    if (current.nextTask == null) {
                        taskMap.remove(key) // no more entries for that DateAndTime so remove altogether
                    } else {
                        taskMap[key] = current.nextTask!! // shift the keys entry to the nextTask node
                    }
                } else {
                    prev.nextTask = current.nextTask
                }
                addCompletedTask(DateCompleted(TaskManager.todayDate, key), value)
            }
            prev = current
            current = current.nextTask
        }
    }

    private fun addCompletedTask(key: DateCompleted, value: TaskDetail) {
        if (completedMap.containsKey(key)) {
            var current = completedMap[key]
            while (current != null) {
                if (current.task == value) return
                current = current.nextTask
            }
            val place = completedMap[key]
            completedMap[key] = TaskNode(value, place)
        } else {
            completedMap[key] = TaskNode(value, null)
        }
    }

    fun getTasksCompleted() : MutableList<TaskCompleted> {
        val taskList = mutableListOf<TaskCompleted>()
        var current : TaskNode?
        for ((key, value) in completedMap) { //key = DateComplete; value = TaskNode
            current = value
            while (current != null) {
                val task = TaskCompleted(key.getDateCompleted(), key.getDueDate(), current.task)
                taskList.add(task)
                current = current.nextTask
            }
        }
        return taskList // TaskCompleted = Date, DateAndTime, TaskDetail
    }

    // gets task completed on a specific date?
    fun getTasksCompletedRange(date : Date) : MutableList<TaskCompleted> {
        val taskList = mutableListOf<TaskCompleted>()
        for ((key, value) in completedMap) { //key = DateComplete; value = TaskNode
            if (key.getDueDate().getDate() == date) {
                var current: TaskNode? = value
                while (current != null) {
                    val task = TaskCompleted(key.getDateCompleted(), key.getDueDate(), current.task)
                    taskList.add(task)
                    current = current.nextTask
                }
            }
        }
        return taskList
    }

    // FireStore Data Saving and Retrieval
    @RequiresApi(Build.VERSION_CODES.O)
    fun initializeDatabase() {
        cleanUpTasks()
        val db = FirebaseFirestore.getInstance()
        val taskCollection = db.collection("users")
            .document(userId!!.uid)
            .collection("tasks")
        loadTasksFromDatabase(taskCollection)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTasksFromDatabase(collection: CollectionReference) {
        collection.get()
            .addOnSuccessListener { result ->
                // val taskMap = TreeMap<String, Task>()
                // private val taskMap = TreeMap<DateAndTime, TaskNode>()
                for (document in result.documents) {
                    val storedTask = document.toObject(TaskStore::class.java)

                    val key = convertUnix(storedTask!!)
                    val value = TaskDetail(storedTask.taskName, storedTask.taskDescription)

                    addTask(key, value)
                }
                // onResult(taskMap)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting tasks", e)
                // onResult(TreeMap()) // return empty TreeMap on failure
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertUnix(storedTask : TaskStore): DateAndTime {
        val unixSeconds = storedTask.dueDateAndTime // Unix timestamp (in seconds)
        val userZoneId = ZoneId.systemDefault() // system time zone
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixSeconds), userZoneId)

        val date = Date(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
        /*var hour = dateTime.hour
        var isPM = false
        if (hour > 12) {
            hour -= 12
            isPM = true
        }
        val time = Time(hour, dateTime.minute, isPM)*/
        val time = Time(dateTime.hour, dateTime.minute)
        return DateAndTime(date, time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storeAllTasks() {
        val db = FirebaseFirestore.getInstance()
        val taskCollection = db.collection("users")
            .document(userId!!.uid)
            .collection("tasks")

        for ((key, value) in taskMap) {
            var current: TaskNode? = value
            while (current != null) {
                val task = TaskStore(current.task.getTaskName(), current.task.getTaskDescription(), key.getUnixTime())
                taskCollection.document().set(task)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Task '${value.toString()}' stored successfully.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error storing task '$value'", e)
                    }
                current = current.nextTask
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun storeTask(task : Task) {
        val db = FirebaseFirestore.getInstance()
        val taskCollection = db.collection("users")
            .document(userId!!.uid)
            .collection("tasks")
        val item = TaskStore(task.getTaskName(), task.getTaskDescription(), task.getDateAndTime().getUnixTime())
        taskCollection.document().set(item)
            .addOnSuccessListener {
                Log.d("Firestore", "Task '${task.toString()}' stored successfully.")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error storing task '$task'", e)
            }
    }

    private fun deleteTask(taskName: String, taskDescription: String, dueDateAndTime: Number) {
        val db = FirebaseFirestore.getInstance()
        val taskRef = db.collection("users")
            .document(userId!!.uid)
            .collection("tasks")

        taskRef
            .whereEqualTo("taskName", taskName)
            .whereEqualTo("taskDescription", taskDescription)
            .whereEqualTo("dueDateAndTime", dueDateAndTime)
            .limit(1) // Only want to delete one match
            .get()
            .addOnSuccessListener { documents ->
                val doc = documents.firstOrNull()
                if (doc != null) {
                    taskRef.document(doc.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Task '${taskName}' successfully deleted.")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error deleting task '$taskName'", e)
                        }
                } else {
                    Log.d("Firestore", "No matching task found.")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error querying tasks: ${e.message}")
            }
    }

    /* auto-delete for completed tasks psychology
     * Completed tasks can be uncompleted but not deleted before a certain time range.
     * This is to provide users with the satisfaction of viewing all their completed tasks, thereby being proud of what they have have accomplished.
     */

    @RequiresApi(Build.VERSION_CODES.O)
    fun cleanUpTasks() {
        // get Today's date, subtract 7 days
        // convert to unix
        // remove all tasks that happened before the unix data
        val currentDate = LocalDateTime.now()
        val targetDate = currentDate.minusDays(7)
        val userZoneId = ZoneId.systemDefault() // gets user's current time zone

        val unixTime = targetDate.atZone(userZoneId).toEpochSecond()

        val db = FirebaseFirestore.getInstance()
        val taskRef = db.collection("users")
            .document(userId!!.uid)
            .collection("tasks")

        // Query to find all documents where the field value is less than the given value
        val query = taskRef.whereLessThan("dueDateAndTime", unixTime)

        // Get the documents that match the query
        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Prepare a batch to delete documents
                val batch = db.batch()

                // Loop through documents in the query result
                for (document in querySnapshot.documents) {
                    // Delete the document
                    batch.delete(document.reference)
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("Firestore", "Documents deleted successfully.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore","Error deleting documents: $e")
                    }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore","Error deleting documents: $e")
            }
    }
    /*private fun convertTaskMapToList(map: TreeMap<DateAndTime, TaskNode>): List<TaskStore> {
        val list = mutableListOf<TaskStore>()
        for ((key, value) in map) {
            var current: TaskNode? = value
            while (current != null) {
                val task = TaskStore(current.task.getTaskName(), current.task.getTaskDescription(), key.getUnixTime())
                list.add(task)
                current = current.nextTask
            }
        }
        return list
    }*/
}