# Task Outline

### Task Data Type
- TaskOutline: Interface for Task data type
- Task: Task data type; implements TaskOutline; currently stores Task Name, Details, Due date and time
- TaskDetail: value in TreeSet that stores only task's name and description
- TaskCompleted: Data type for completed tasks; used to completed task TreeSet and Adapter for viewing
- TaskCompleted: Data type for storing and retrieving tasks in firestore

### Task Data Structure
- TaskManager: local task management using Mutable Lists to make viewing tasks quicker for the user
- TaskDataStructure: global task storage using TreeMap (Red-Black BST under the hood) to store all tasks. Also implements the logic for storing and retrieving tasks w/ Firebase

### Task Middle-man
- Task Listener: interface that facilitates communication between Task Adapters, View Models, and Data Structures

### Task View Models and Core functionality
- CalendarFragment: shows tasks due on a certain day and whether the tasks were completed or not. Users can mark tasks as completed in this fragment.
- TaskFragment: fragment that shows all the tasks categorized as past due, due today, due later and completed. Functionality for editing, marking tasks as complete, and deleting tasks are also in this fragment.
- TaskActivity: activity that adds tasks to global data structure (TreeSet implementation in TaskDataStructure) and updates stored local data (Multiple Mutable Lists in TaskManager). Newly added tasks are shown inside the activity until page reloads/exits

### Task Adapters
- TaskAdapterActivity: used for showing tasks recently added in TaskActivity
- TaskAdapterCompleted & CalendarAdapterCompleted: used for showing completed tasks in a different style than scheduled tasks in TaskFragment
- TaskAdapterList: dynamically shows tasks that are yet to complete in TaskFragment
- CalendarAdapter: dynamically changes and shows tasks that are scheduled for a selected day

### Miscellaneous
- Date: Takes year, month and date as input. Technically, a child class for DateAndTime.
- Time: Takes hour (0 - 23) and minute (0 - 59) as input. Automatically calculates AM or PM.
- DateAndTime: Takes the above classes Date and Time as input. Alternatively also takes unix time as input.
- DateCompleted: Takes two DateAndTime objects or two unix timestamps as inputs, one for the completed date and the other for the due date. Primarily used in the completed task storage system.