
package com.example.myapplication1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Task 2: UI State
data class AttendanceUiState(
    val sessionTitle: String = "",
    val checkedInCount: Int = 0,
    val isCheckedIn: Boolean = false
)

// Task 3: Stateless Screen
@Composable
fun AttendanceScreen(
    uiState: AttendanceUiState,
    onCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = uiState.sessionTitle)

        Text(text = "Checked in: ${uiState.checkedInCount}")

        Button(
            onClick = onCheckIn,
            enabled = !uiState.isCheckedIn
        ) {
            Text(
                text = if (uiState.isCheckedIn) {
                    "Checked in"
                } else {
                    "Check in"
                }
            )
        }
    }
}

// Preview 1
@Preview(showBackground = true, name = "Not checked in yet")
@Composable
private fun AttendanceScreenPreviewNotCheckedIn() {
    MaterialTheme {
        Surface {
            AttendanceScreen(
                uiState = AttendanceUiState(
                    sessionTitle = "CSC 402 - Week 4",
                    checkedInCount = 12,
                    isCheckedIn = false
                ),
                onCheckIn = {}
            )
        }
    }
}

// Preview 2
@Preview(showBackground = true, name = "Already checked in")
@Composable
private fun AttendanceScreenPreviewCheckedIn() {
    MaterialTheme {
        Surface {
            AttendanceScreen(
                uiState = AttendanceUiState(
                    sessionTitle = "CSC 402 - Week 4",
                    checkedInCount = 13,
                    isCheckedIn = true
                ),
                onCheckIn = {}
            )
        }
    }
}

// Task 4: Route owns the state
@Composable
fun AttendanceRoute() {
    var uiState by remember {
        mutableStateOf(
            AttendanceUiState(
                sessionTitle = "CSC 402 - Week 4",
                checkedInCount = 12
            )
        )
    }

    AttendanceScreen(
        uiState = uiState,
        onCheckIn = {
            uiState = uiState.copy(
                checkedInCount = uiState.checkedInCount + 1,
                isCheckedIn = true
            )
        }
    )
}

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AttendanceRoute()
                }
            }
        }
    }
}
