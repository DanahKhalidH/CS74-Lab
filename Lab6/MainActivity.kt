package com.example.myapplication1

import android.os.Bundle
import android.widget.RatingBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication1.ui.theme.MyApplication1Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplication1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CourseRatingRoute(
                        code = "CSC 402",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun StarRating(
    stars: Float,
    onStarsChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            RatingBar(context).apply {
                numStars = 5
                stepSize = 0.5f

                setOnRatingBarChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        onStarsChange(value)
                    }
                }
            }
        },
        update = { view ->
            if (view.rating != stars) {
                view.rating = stars
            }
        },
        modifier = modifier
    )
}

data class CourseRatingUiState(
    val code: String = "",
    val stars: Float = 0f
)

@Composable
fun CourseRatingScreen(
    uiState: CourseRatingUiState,
    onStarsChange: (Float) -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Rate ${uiState.code}")

        StarRating(
            stars = uiState.stars,
            onStarsChange = onStarsChange
        )

        Text(text = "You gave ${uiState.stars} stars")

        Button(
            onClick = onResetClick
        ) {
            Text(text = "Reset")
        }
    }
}

@Composable
fun CourseRatingRoute(
    code: String,
    modifier: Modifier = Modifier
) {
    var stars by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    CourseRatingScreen(
        uiState = CourseRatingUiState(
            code = code,
            stars = stars
        ),
        onStarsChange = { newStars ->
            stars = newStars
        },
        onResetClick = {
            stars = 0f
        },
        modifier = modifier
    )
}
