package com.example.learnready.ui.quiz

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
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
fun QuizScreen(
    moduleId: Int,
    onBackClick: () -> Unit,
    onQuizComplete: (score: Int, total: Int, moduleId: Int) -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSubmitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onQuizComplete(uiState.score, uiState.askedQuestions.size, moduleId)
        }
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text(
                    "Submit Quiz?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                val answered = uiState.selectedAnswers.size
                val total = uiState.askedQuestions.size
                Text(
                    "You have answered $answered out of $total questions.\n\nAre you sure you want to submit?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        viewModel.submitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        uiState.moduleName,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Timer
                    val minutes = uiState.timeRemainingSeconds / 60
                    val seconds = uiState.timeRemainingSeconds % 60
                    val timerColor = if (uiState.timeRemainingSeconds < 300) Color.Red else TextSecondary

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = timerColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            color = timerColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Submit button
                    TextButton(onClick = { showSubmitDialog = true }) {
                        Text(
                            "Submit",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite
                )
            )
        },
        containerColor = BackgroundWhite
    ) { paddingValues ->
        if (uiState.isLoading || uiState.askedQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = PrimaryBlue)
                } else {
                    Text(
                        "No questions available for this module.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Progress indicator
                QuizProgressBar(
                    current = uiState.currentQuestionIndex + 1,
                    total = minOf(10, uiState.allQuestions.size),
                    answeredCount = uiState.selectedAnswers.size
                )

                // Question content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    val question = uiState.askedQuestions[uiState.currentQuestionIndex]

                    // Question number badge
                    Surface(
                        color = SoftBlue,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Question ${uiState.currentQuestionIndex + 1}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Question text
                    Text(
                        text = question.questionEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 24.dp),
                        lineHeight = 26.sp
                    )

                    // Options
                    val options = viewModel.parseOptions(question.optionsEn)
                    val selectedAnswer = uiState.selectedAnswers[uiState.currentQuestionIndex]

                    options.forEachIndexed { index, option ->
                        val optionLabel = ('A' + index).toString()
                        val isSelected = selectedAnswer == option

                        OptionCard(
                            label = optionLabel,
                            text = option,
                            isSelected = isSelected,
                            isChecking = uiState.isChecking,
                            isCorrect = option == question.correctAnswer,
                            onClick = {
                                if (!uiState.isChecking) {
                                    viewModel.selectAnswer(uiState.currentQuestionIndex, option)
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }

                // Feedback Card
                if (uiState.isChecking) {
                    val currentQ = uiState.askedQuestions[uiState.currentQuestionIndex]
                    val isCorrect = uiState.selectedAnswers[uiState.currentQuestionIndex] == currentQ.correctAnswer
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isCorrect) SuccessGreen.copy(alpha = 0.1f) else Color(0xFFFEF2F2),
                        border = BorderStroke(1.dp, if (isCorrect) SuccessGreen else Color(0xFFEF4444)),
                        shadowElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (isCorrect) Icons.Default.CheckCircle else Icons.Default.CheckCircle, // using CheckCircle as placeholder for cross
                                    contentDescription = "Result",
                                    tint = if (isCorrect) SuccessGreen else Color(0xFFEF4444)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    if (isCorrect) "Excellent Work!" else "Not quite right",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) SuccessGreen else Color(0xFFEF4444)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                currentQ.explanationEn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }

                // Navigation buttons
                NavigationBar(
                    currentIndex = uiState.currentQuestionIndex,
                    totalQuestions = minOf(10, uiState.allQuestions.size),
                    hasAnswered = uiState.selectedAnswers.containsKey(uiState.currentQuestionIndex),
                    isChecking = uiState.isChecking,
                    onCheck = { viewModel.checkAnswer() },
                    onNext = { viewModel.nextQuestion() },
                    onSubmit = { viewModel.submitQuiz() }
                )
            }
        }
    }
}

@Composable
private fun QuizProgressBar(
    current: Int,
    total: Int,
    answeredCount: Int
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Progress",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Text(
                "$answeredCount/$total answered",
                style = MaterialTheme.typography.labelMedium,
                color = PrimaryBlue,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { current.toFloat() / total.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PrimaryBlue,
            trackColor = SoftBlue,
        )
    }
}

@Composable
private fun OptionCard(
    label: String,
    text: String,
    isSelected: Boolean,
    isChecking: Boolean = false,
    isCorrect: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = when {
        isChecking && isSelected && isCorrect -> SuccessGreen
        isChecking && isSelected && !isCorrect -> Color(0xFFEF4444)
        isChecking && !isSelected && isCorrect -> SuccessGreen
        isSelected -> PrimaryBlue
        else -> Color(0xFFE2E8F0)
    }
    
    val bgColor = when {
        isChecking && isSelected && isCorrect -> SuccessGreen.copy(alpha = 0.1f)
        isChecking && isSelected && !isCorrect -> Color(0xFFFEF2F2)
        isChecking && !isSelected && isCorrect -> SuccessGreen.copy(alpha = 0.05f)
        isSelected -> SoftBlue.copy(alpha = 0.5f)
        else -> CardWhite
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(
            width = if (isSelected || (isChecking && isCorrect)) 2.dp else 1.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Option label circle
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = if (isSelected) PrimaryBlue else Color(0xFFF1F5F9),
                shadowElevation = 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val labelColor = when {
                        isChecking && isSelected && isCorrect -> Color.White
                        isChecking && isSelected && !isCorrect -> Color.White
                        isSelected -> Color.White
                        else -> TextSecondary
                    }
                    Text(
                        text = label,
                        color = labelColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (isChecking && isCorrect) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = SuccessGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else if (isSelected && !isChecking) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun NavigationBar(
    currentIndex: Int,
    totalQuestions: Int,
    hasAnswered: Boolean,
    isChecking: Boolean,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = CardWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Spacer to keep layout balanced
            Spacer(modifier = Modifier.width(8.dp))

            // Question dots indicator
            Text(
                "${currentIndex + 1} / $totalQuestions",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            // Button
            if (!isChecking) {
                Button(
                    onClick = onCheck,
                    enabled = hasAnswered,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Check", fontWeight = FontWeight.SemiBold)
                }
            } else if (currentIndex < totalQuestions - 1) {
                Button(
                    onClick = onNext,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Continue", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Button(
                    onClick = onSubmit,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Submit",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Finish", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
