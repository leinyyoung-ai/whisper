package com.facesore.english.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facesore.english.ui.theme.FaceSoreEnglishTheme
import com.facesore.english.vocabulary.VocabularyTestViewModel
import com.facesore.english.vocabulary.sampleWords

@Composable
fun VocabularyTestScreen(viewModel: VocabularyTestViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isFinished) {
            Text(text = "Vocabulary test complete!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Your estimated vocabulary size is: ${uiState.score}", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(text = "Do you know this word?", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Placeholder for the image
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                 // In a real app, you would load the image from currentWord.imageUrl
                 // For now, we just show the word as text.
                Text(text = uiState.currentWord?.word ?: "", style = MaterialTheme.typography.headlineLarge)
            }


            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.answer(true) }) {
                    Text("I know")
                }

                Button(onClick = { viewModel.answer(false) }) {
                    Text("I don't know")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabularyTestScreenPreview() {
    FaceSoreEnglishTheme {
        VocabularyTestScreen()
    }
}
