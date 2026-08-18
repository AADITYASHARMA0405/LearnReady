package com.example.learnready.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.outlined.*
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnready.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    onBackClick: () -> Unit,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Check if all questions are answered
    val allAnswered = uiState.questions.isNotEmpty() && 
            uiState.responses.values.none { it == 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feedback Survey", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Outlined.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBlue,
                    titleContentColor = CardWhite,
                    navigationIconContentColor = CardWhite
                )
            )
        }
    ) { padding ->
        if (uiState.isComplete) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = SuccessGreen.copy(alpha = 0.15f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.EmojiEmotions,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    "Thank You!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Your feedback helps us improve LearnReady for everyone.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Return to Profile", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Help us improve!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        "Please rate your agreement with the following statements.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(uiState.questions) { question ->
                    val currentValue = uiState.responses[question.id] ?: 0
                    SurveyQuestionCard(
                        question = question,
                        currentValue = currentValue,
                        onValueChange = { viewModel.updateResponse(question.id, it) }
                    )
                }

                item {
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = viewModel::submitSurvey,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = allAnswered && !uiState.isSubmitting,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = CardWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Submit Survey", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    if (!allAnswered) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Please answer all questions to submit.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SurveyQuestionCard(
    question: SurveyQuestion,
    currentValue: Int,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = question.textEn,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(16.dp))
            
            // Likert Scale UI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1: Strongly Disagree
                LikertOption(value = 1, currentValue = currentValue, color = Color(0xFFE53935), onSelect = onValueChange)
                // 2: Disagree
                LikertOption(value = 2, currentValue = currentValue, color = Color(0xFFFB8C00), onSelect = onValueChange)
                // 3: Neutral
                LikertOption(value = 3, currentValue = currentValue, color = Color(0xFFFDD835), onSelect = onValueChange)
                // 4: Agree
                LikertOption(value = 4, currentValue = currentValue, color = Color(0xFF43A047), onSelect = onValueChange)
                // 5: Strongly Agree
                LikertOption(value = 5, currentValue = currentValue, color = Color(0xFF1E88E5), onSelect = onValueChange)
            }
            
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Strongly\nDisagree", style = MaterialTheme.typography.labelSmall, color = TextSecondary, textAlign = TextAlign.Center)
                Text("Strongly\nAgree", style = MaterialTheme.typography.labelSmall, color = TextSecondary, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun LikertOption(
    value: Int,
    currentValue: Int,
    color: Color,
    onSelect: (Int) -> Unit
) {
    val isSelected = value == currentValue
    
    Surface(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .clickable { onSelect(value) },
        shape = CircleShape,
        color = if (isSelected) color else color.copy(alpha = 0.1f),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = value.toString(),
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else color,
                fontSize = 16.sp
            )
        }
    }
}
