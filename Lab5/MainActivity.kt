
package com.example.myapplication1
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Course(
    val code: String,
    val name: String,
    val creditHours: Int
)

val courses = listOf(
    Course("CSC 402", "Mobile Application Development", 3),
    Course("CSC 301", "Database Systems", 3),
    Course("CSC 302", "Software Engineering", 3)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Lab5App()
            }
        }
    }
}

@Composable
fun Lab5App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onCoursesClick = {
                    navController.navigate("courses")
                }
            )
        }

        composable("courses") {
            CourseListScreen(
                onCourseClick = { code ->
                    navController.navigate(
                        "detail/${Uri.encode(code)}"
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detail/{code}",
            arguments = listOf(
                navArgument("code") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val code = entry.arguments?.getString("code") ?: ""

            CourseDetailScreen(
                code = code,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCoursesClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CSC 402 - Home") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to the University App",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    try {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.iau.edu.sa/")
                        )
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No application found to open the website",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("University Website")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    try {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:")
                        )
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No dialer application found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Call Department")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCoursesClick
            ) {
                Text("Browse Courses")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    onCourseClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course List") },
                navigationIcon = {
                    Button(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            courses.forEach { course ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCourseClick(course.code)
                    }
                ) {
                    Text("${course.code} - ${course.name}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    code: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val course = courses.find { it.code == code }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course Details") },
                navigationIcon = {
                    Button(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (course != null) {
                Text(
                    text = "Course Code: ${course.code}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("Course Name: ${course.name}")
                Text("Credit Hours: ${course.creditHours}")
            } else {
                Text("Course not found")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val shareIntent = Intent(
                        Intent.ACTION_SEND
                    ).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Course Code: ${course?.code}\n" +
                                    "Course Name: ${course?.name}\n" +
                                    "Credit Hours: ${course?.creditHours}"
                        )
                    }

                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Share Course"
                        )
                    )
                }
            ) {
                Text("Share Course")
            }
        }
    }
}
