package com.howbigisit.app

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * The four scientifically rigorous size classifications.
 * Deliberately not tied to any actual measurement.
 */
private val VERDICTS = listOf("Little", "Medium", "Kind of big", "Big")

private sealed interface Screen {
    data object Capture : Screen
    data class Asking(val photo: Bitmap) : Screen
    data class Verdict(val photo: Bitmap, val verdict: String) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HowBigIsItApp(onDecline = { finish() })
                }
            }
        }
    }
}

@Composable
private fun HowBigIsItApp(onDecline: () -> Unit) {
    var screen by remember { mutableStateOf<Screen>(Screen.Capture) }

    val takePhoto = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) screen = Screen.Asking(bitmap)
    }

    when (val s = screen) {
        is Screen.Capture -> CaptureScreen(onTakePhoto = { takePhoto.launch(null) })

        is Screen.Asking -> {
            PhotoBackdrop(photo = s.photo)
            AlertDialog(
                onDismissRequest = { /* the question must be answered */ },
                title = { Text(stringResource(R.string.the_question)) },
                confirmButton = {
                    TextButton(onClick = {
                        screen = Screen.Verdict(s.photo, VERDICTS.random())
                    }) { Text(stringResource(R.string.yes)) }
                },
                dismissButton = {
                    TextButton(onClick = onDecline) { Text(stringResource(R.string.no)) }
                }
            )
        }

        is Screen.Verdict -> VerdictScreen(
            photo = s.photo,
            verdict = s.verdict,
            onAgain = { screen = Screen.Capture }
        )
    }
}

@Composable
private fun CaptureScreen(onTakePhoto: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.the_question),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onTakePhoto,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(stringResource(R.string.take_photo))
        }
    }
}

@Composable
private fun PhotoBackdrop(photo: Bitmap) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = photo.asImageBitmap(),
            contentDescription = "The thing in question",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun VerdictScreen(photo: Bitmap, verdict: String, onAgain: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = photo.asImageBitmap(),
            contentDescription = "The thing in question",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = verdict,
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp)
        )
        Button(
            onClick = onAgain,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(stringResource(R.string.again))
        }
    }
}
