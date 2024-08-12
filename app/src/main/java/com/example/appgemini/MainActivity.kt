package com.example.appgemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appgemini.ui.theme.AppGeminiTheme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = ApiKey.GEMINI_API_KEY
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppGeminiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeminiTextField(generativeModel)
                }
            }
        }
    }
}

@Composable
fun GeminiTextField(generativeModel: GenerativeModel) {
    var inputText by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    val scope = CoroutineScope(Dispatchers.Main)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = responseText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter Prompt") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    val prompt = inputText
                    val response = generativeModel.generateContent(prompt)
                    responseText = response.text.toString()
                }
            },
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text("Generate")
        }


    }
}
