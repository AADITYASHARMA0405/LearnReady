package com.example.learnready.ui.quiz

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnready.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

import androidx.activity.compose.BackHandler
import com.example.learnready.ui.quiz.QuizViewModel

@Composable
fun QuizResultScreen(
    score: Int,
    total: Int,
    moduleId: Int,
    quizViewModel: QuizViewModel? = null,
    onBackToHome: () -> Unit,
    onRetryQuiz: () -> Unit
) {
    BackHandler {
        onBackToHome()
    }

    val percentage = if (total > 0) (score * 100) / total else 0
    val uiState by quizViewModel?.uiState?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }
    val scoreColor = when {
        percentage >= 70 -> SuccessGreen
        percentage >= 40 -> Orange
        else -> Color(0xFFEF4444)
    }

    val performanceMessage = when {
        percentage >= 90 -> "🎉 Outstanding! You're a star!"
        percentage >= 70 -> "👏 Great job! Keep it up!"
        percentage >= 50 -> "👍 Good effort! Room for improvement."
        percentage >= 40 -> "📚 Keep practicing, you're getting there!"
        else -> "💪 Don't give up! Review the material and try again."
    }

    val performanceEmoji = when {
        percentage >= 90 -> "🏆"
        percentage >= 70 -> "⭐"
        percentage >= 50 -> "📖"
        percentage >= 40 -> "💡"
        else -> "🔄"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Confetti for high scores
        if (percentage >= 70) {
            ConfettiAnimation()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Trophy icon for high scores
            if (percentage >= 70) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = SoftGreen,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = SuccessGreen,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // Animated circular score indicator
            AnimatedScoreCircle(
                percentage = percentage,
                scoreColor = scoreColor
            )

            Spacer(Modifier.height(24.dp))

            // Score text
            Text(
                text = "$score out of $total correct",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            // Performance message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = scoreColor.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = performanceEmoji,
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = performanceMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    label = "Score",
                    value = "$percentage%",
                    color = scoreColor
                )
                StatCard(
                    label = "Correct",
                    value = "$score",
                    color = SuccessGreen
                )
                StatCard(
                    label = "Wrong",
                    value = "${total - score}",
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(Modifier.height(32.dp))

            // Question Breakdown
            if (uiState != null && uiState!!.askedQuestions.isNotEmpty()) {
                Text(
                    text = "Question Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Spacer(Modifier.height(16.dp))
                
                uiState!!.askedQuestions.forEachIndexed { index, question ->
                    val selectedAns = uiState!!.selectedAnswers[index]
                    val isCorrect = selectedAns == question.correctAnswer
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.05f) else Color(0xFFFEF2F2)),
                        border = BorderStroke(1.dp, if (isCorrect) SuccessGreen.copy(alpha = 0.3f) else Color(0xFFFCA5A5))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    "${index + 1}.",
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    question.questionEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Your answer: ${selectedAns ?: "Skipped"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCorrect) SuccessGreen else Color(0xFFEF4444)
                            )
                            if (!isCorrect) {
                                Text(
                                    "Correct answer: ${question.correctAnswer}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            // Action buttons
            Button(
                onClick = onRetryQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Retry",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Retry Quiz",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(14.dp))

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue)
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Back to Home",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AnimatedScoreCircle(
    percentage: Int,
    scoreColor: Color
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.toFloat(),
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "scoreAnimation"
    )

    Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            // Background circle
            drawCircle(
                color = Color(0xFFF1F5F9),
                radius = size.minDimension / 2,
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )

            // Score arc
            drawArc(
                color = scoreColor,
                startAngle = -90f,
                sweepAngle = (animatedPercentage / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${animatedPercentage.toInt()}%",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = scoreColor
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun ConfettiAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")

    val particles = remember {
        List(30) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 8f + 4f,
                color = listOf(
                    PrimaryBlue, SuccessGreen, Purple, Orange,
                    Color(0xFFEF4444), Color(0xFFF59E0B)
                ).random(),
                speed = Random.nextFloat() * 2f + 1f,
                angle = Random.nextFloat() * 360f
            )
        }
    }

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProgress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val yPos = ((particle.y + progress * particle.speed) % 1.2f) * size.height
            val xOffset = sin(progress * 6.28f + particle.angle.toDouble()).toFloat() * 30f
            val xPos = particle.x * size.width + xOffset

            drawCircle(
                color = particle.color.copy(alpha = 0.7f),
                radius = particle.size,
                center = Offset(xPos, yPos)
            )
        }
    }
}

private data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color,
    val speed: Float,
    val angle: Float
)
