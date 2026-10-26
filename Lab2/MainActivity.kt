
package com.example.myapplication1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun task1(): List<String> {
    val university = "Imam Abdulrahman Bin Faisal University"
    val courseCode = "CSC 402"
    val creditHours = 3
    var studentsEnrolled = 28
    val isElective = false

    val results = mutableListOf<String>()
    results.add("$courseCode has $studentsEnrolled students enrolled ($creditHours credit hours).")

    studentsEnrolled += 2
    results.add("$courseCode has $studentsEnrolled students enrolled ($creditHours credit hours).")
    results.add("University name length: ${university.length}")
    results.add("Is elective: $isElective")

    return results
}

// Task 2: Functions
fun greetStudent(name: String, course: String = "CSC 402"): String {
    return "Welcome to $course, $name!"
}

fun finalMark(
    quizzes: Double,
    project: Double,
    finalExam: Double
): Double = quizzes * 0.10 + project * 0.70 + finalExam * 0.20

fun printBanner(title: String, width: Int = 40): String {
    return "${"-".repeat(width)}\n$title\n${"-".repeat(width)}"
}

fun task2(): List<String> {
    return listOf(
        printBanner("CSC 402 Lab 2"),
        greetStudent("Sara"),
        greetStudent("Aseel", "CSC 402"),
        "Final mark: ${finalMark(90.0, 85.0, 95.0)}",
        "Named arguments: ${finalMark(project = 85.0, finalExam = 95.0, quizzes = 90.0)}"
    )
}

// Task 3: Conditional expressions
fun task3(mark: Int): String {
    return if (mark >= 60) "Pass" else "Fail"
}

// Task 4: Null safety
fun task4(name: String?): List<String> {
    val results = mutableListOf<String>()
    results.add(name?.uppercase() ?: "UNKNOWN")
    name?.let { results.add("Name length: ${it.length}") }
    return results
}

// Task 5: Data classes and list operations
data class Student(val name: String, val mark: Int)

fun task5(): List<String> {
    val students = listOf(
        Student("Sara", 90),
        Student("Aseel", 85),
        Student("Nora", 55)
    )

    return buildList {
        add("All students:")
        students.forEach { add("${it.name}: ${it.mark}") }
        add("Passed students:")
        students.filter { it.mark >= 60 }
            .forEach { add(it.name) }
        add("Student names: ${students.map { it.name }}")
    }
}

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Lab2Screen()
            }
        }
    }
}

@Composable
fun Lab2Screen() {
    var name by remember { mutableStateOf("") }
    var count by remember { mutableIntStateOf(0) }
    var showResults by remember { mutableStateOf(false) }

    val students = listOf(
        Student("Sara", 90),
        Student("Aseel", 85),
        Student("Nora", 55)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "CSC 402 - Lab 2",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = if (name.isBlank()) {
                    "Welcome to CSC 402!"
                } else {
                    "Welcome, $name!"
                }
            )
        }

        item {
            Text("Counter: $count")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { count++ }) {
                    Text("Increase")
                }
                Button(onClick = { count = 0 }) {
                    Text("Reset")
                }
            }
        }

        item {
            Button(
                onClick = { showResults = !showResults },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showResults) "Hide Results" else "Run All Tasks 1–5")
            }
        }

        if (showResults) {
            item {
                Text("Task 1: Variables", style = MaterialTheme.typography.titleLarge)
            }
            items(task1()) { result ->
                Text(result)
            }

            item {
                Text("Task 2: Functions", style = MaterialTheme.typography.titleLarge)
            }
            items(task2()) { result ->
                Text(result)
            }

            item {
                Text("Task 3: Conditions", style = MaterialTheme.typography.titleLarge)
                Text("Mark 85: ${task3(85)}")
                Text("Mark 45: ${task3(45)}")
            }

            item {
                Text("Task 4: Null Safety", style = MaterialTheme.typography.titleLarge)
                task4("Aseel").forEach { Text(it) }
                task4(null).forEach { Text(it) }
            }

            item {
                Text("Task 5: Students", style = MaterialTheme.typography.titleLarge)
            }
            items(task5()) { result ->
                Text(result)
            }
        }

        item {
            Text("Student List", style = MaterialTheme.typography.titleLarge)
        }

        items(students) { student ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Name: ${student.name}")
                    Text("Mark: ${student.mark}")
                    Text("Result: ${task3(student.mark)}")
                }
            }
        }
    }
}
