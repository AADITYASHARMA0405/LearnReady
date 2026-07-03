package com.example.learnready.ui.mock

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnready.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockInterviewScreen(
    onBackClick: () -> Unit,
    viewModel: MockInterviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Start interview on first composition
    LaunchedEffect(Unit) {
        viewModel.startInterview(5)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isComplete) "Interview Results"
                        else "Mock Interview",
                        fontWeight = FontWeight.Bold
                    )
                },
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
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PrimaryBlue)
                        Spacer(Modifier.height(16.dp))
                        Text("Preparing your interview...", color = TextSecondary)
                    }
                }
            }
            uiState.isComplete -> {
                InterviewResultsContent(
                    uiState = uiState,
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {
                InterviewQuestionContent(
                    uiState = uiState,
                    onAnswerChange = viewModel::updateAnswer,
                    onSubmitAnswer = viewModel::submitAnswer,
                    onCommScoreChange = viewModel::updateCommScore,
                    onConfScoreChange = viewModel::updateConfScore,
                    onTechScoreChange = viewModel::updateTechScore,
                    onSubmitRating = viewModel::submitRating,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun InterviewQuestionContent(
    uiState: MockInterviewUiState,
    onAnswerChange: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onCommScoreChange: (Int) -> Unit,
    onConfScoreChange: (Int) -> Unit,
    onTechScoreChange: (Int) -> Unit,
    onSubmitRating: () -> Unit,
    modifier: Modifier = Modifier
) {
    val question = uiState.questions[uiState.currentIndex]
    val progress by animateFloatAsState(
        targetValue = (uiState.currentIndex + 1).toFloat() / uiState.questions.size,
        animationSpec = tween(600),
        label = "progress"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Progress header
        Surface(color = DeepBlue) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Question ${uiState.currentIndex + 1} of ${uiState.questions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CardWhite.copy(alpha = 0.9f)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (question.categoryTag) {
                            "communication" -> AccentGold.copy(alpha = 0.2f)
                            "technical" -> PrimaryBlue.copy(alpha = 0.3f)
                            else -> SuccessGreen.copy(alpha = 0.2f)
                        }
                    ) {
                        Text(
                            question.categoryTag.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CardWhite
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AccentGold,
                    trackColor = CardWhite.copy(alpha = 0.2f)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Question card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Interviewer asks:",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        question.questionEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedContent(
                targetState = uiState.phase,
                transitionSpec = {
                    fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 } togetherWith
                            fadeOut(tween(200))
                },
                label = "phase"
            ) { phase ->
                when (phase) {
                    InterviewPhase.ANSWERING -> {
                        Column {
                            // Answer input
                            OutlinedTextField(
                                value = uiState.currentAnswer,
                                onValueChange = onAnswerChange,
                                label = { Text("Your answer") },
                                placeholder = { Text("Type your response here...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 150.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0),
                                    focusedContainerColor = CardWhite,
                                    unfocusedContainerColor = CardWhite
                                ),
                                maxLines = 8
                            )

                            // Rubric hints
                            if (question.rubricHintsEn.isNotBlank()) {
                                Spacer(Modifier.height(12.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = AccentGold.copy(alpha = 0.08f)
                                    )
                                ) {
                                    Row(modifier = Modifier.padding(12.dp)) {
                                        Icon(
                                            Icons.Outlined.Lightbulb,
                                            contentDescription = null,
                                            tint = AccentGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            question.rubricHintsEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = onSubmitAnswer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                enabled = uiState.currentAnswer.isNotBlank()
                            ) {
                                Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Submit Answer", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    InterviewPhase.RATING -> {
                        Column {
                            Text(
                                "Self-Assessment",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                "Rate your response honestly on each dimension (1-5):",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(Modifier.height(16.dp))

                            RatingSlider(
                                label = "Communication",
                                icon = Icons.Outlined.Chat,
                                value = uiState.currentCommScore,
                                onValueChange = onCommScoreChange,
                                color = PrimaryBlue
                            )
                            Spacer(Modifier.height(12.dp))
                            RatingSlider(
                                label = "Confidence",
                                icon = Icons.Outlined.Psychology,
                                value = uiState.currentConfScore,
                                onValueChange = onConfScoreChange,
                                color = AccentGold
                            )
                            Spacer(Modifier.height(12.dp))
                            RatingSlider(
                                label = "Technical Accuracy",
                                icon = Icons.Outlined.Code,
                                value = uiState.currentTechScore,
                                onValueChange = onTechScoreChange,
                                color = SuccessGreen
                            )

                            Spacer(Modifier.height(24.dp))
                            Button(
                                onClick = onSubmitRating,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                            ) {
                                Text(
                                    if (uiState.currentIndex + 1 < uiState.questions.size) "Next Question"
                                    else "Finish Interview",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    if (uiState.currentIndex + 1 < uiState.questions.size) Icons.Outlined.ArrowForward
                                    else Icons.Outlined.EmojiEvents,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    InterviewPhase.COMPLETE -> { /* Handled by parent */ }
                }
            }
        }
    }
}

@Composable
private fun RatingSlider(
    label: String,
    icon: ImageVector,
    value: Int,
    onValueChange: (Int) -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.1f)
                ) {
                    Text(
                        "$value / 5",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Slider(
                value = value.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = 1f..5f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = color,
                    activeTrackColor = color,
                    inactiveTrackColor = color.copy(alpha = 0.2f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Poor", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Text("Excellent", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun InterviewResultsContent(
    uiState: MockInterviewUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Trophy
        Surface(
            shape = CircleShape,
            color = AccentGold.copy(alpha = 0.15f),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.EmojiEvents,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "Interview Complete!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "${uiState.questions.size} questions answered",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(Modifier.height(24.dp))

        // Overall score
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Overall Score",
                    style = MaterialTheme.typography.labelLarge,
                    color = CardWhite.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "%.1f / 5.0".format(uiState.overallScore),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    getScoreLabel(uiState.overallScore),
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardWhite.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Breakdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScoreCard(
                label = "Comm.",
                score = uiState.avgCommunication,
                icon = Icons.Outlined.Chat,
                color = PrimaryBlue,
                modifier = Modifier.weight(1f)
            )
            ScoreCard(
                label = "Confidence",
                score = uiState.avgConfidence,
                icon = Icons.Outlined.Psychology,
                color = AccentGold,
                modifier = Modifier.weight(1f)
            )
            ScoreCard(
                label = "Technical",
                score = uiState.avgTechnical,
                icon = Icons.Outlined.Code,
                color = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Tips
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Tips for Improvement",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(Modifier.height(8.dp))
                val weakest = listOf(
                    "Communication" to uiState.avgCommunication,
                    "Confidence" to uiState.avgConfidence,
                    "Technical" to uiState.avgTechnical
                ).minByOrNull { it.second }

                val tips = when (weakest?.first) {
                    "Communication" -> listOf(
                        "Practice the STAR method for behavioral answers",
                        "Record yourself answering and review",
                        "Use clear, concise sentences"
                    )
                    "Confidence" -> listOf(
                        "Prepare answers for common questions beforehand",
                        "Practice with a friend or in front of a mirror",
                        "Remember: it's okay to take a moment to think"
                    )
                    else -> listOf(
                        "Review core CS fundamentals regularly",
                        "Practice coding problems on paper",
                        "Explain solutions out loud while solving"
                    )
                }
                tips.forEach { tip ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("•", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            tip,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Back to AI Assistant", fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ScoreCard(
    label: String,
    score: Float,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                "%.1f".format(score),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getScoreLabel(score: Float): String = when {
    score >= 4.5f -> "Outstanding!"
    score >= 3.5f -> "Great job!"
    score >= 2.5f -> "Good, keep practicing!"
    score >= 1.5f -> "Room for improvement"
    else -> "Keep trying!"
}
