package com.example.learnready.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnready.ui.components.SubjectCard
import com.example.learnready.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSubjectClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "LearnReady",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                        Text(
                            text = "Employability Skills Platform",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardWhite
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Greeting
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome back,",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                text = uiState.userName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = NavyBlue
                            )
                        }
                        // Avatar with initials
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = PrimaryBlue
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = uiState.userName.take(1).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Job Readiness Ring + Stats
                item {
                    JobReadinessCard(
                        readinessPercent = uiState.overallReadiness,
                        modulesCompleted = uiState.modulesCompleted,
                        averageScore = uiState.averageQuizScore,
                        streak = uiState.currentStreak
                    )
                }

                // Today's Plan
                if (uiState.todaysPlan.isNotEmpty()) {
                    item {
                        SectionHeader(
                            icon = Icons.Outlined.Assignment,
                            title = "Today's Plan"
                        )
                    }
                    items(uiState.todaysPlan) { plan ->
                        StudyPlanCard(plan = plan)
                    }
                }

                // Quiz Performance Chart
                if (uiState.recentQuizScores.isNotEmpty()) {
                    item {
                        QuizPerformanceChart(scores = uiState.recentQuizScores)
                    }
                }

                // Subjects
                item {
                    SectionHeader(
                        icon = Icons.Outlined.MenuBook,
                        title = "Your Subjects"
                    )
                }

                items(uiState.subjects) { subjectWithLessons ->
                    val colorHex = subjectWithLessons.subject.color
                    val bgColor = try {
                        Color(android.graphics.Color.parseColor(colorHex))
                    } catch (e: Exception) {
                        SoftBlue
                    }
                    SubjectCard(
                        title = subjectWithLessons.subject.nameEn,
                        subtitle = "${subjectWithLessons.lessonCount} Lessons",
                        backgroundColor = bgColor,
                        onClick = { onSubjectClick(subjectWithLessons.subject.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

// ---------- Section Header ----------

@Composable
fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyBlue
        )
    }
}

// ---------- Job Readiness Card with Ring ----------

@Composable
fun JobReadinessCard(
    readinessPercent: Int,
    modulesCompleted: Int,
    averageScore: Int,
    streak: Int
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DeepBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Job Readiness Index",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))

            ReadinessRing(percent = readinessPercent)

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatChip(value = "$modulesCompleted", label = "Modules", icon = Icons.Outlined.TaskAlt)
                StatChip(value = "$averageScore%", label = "Accuracy", icon = Icons.Outlined.TrendingUp)
                StatChip(value = "$streak", label = "Streak", icon = Icons.Outlined.LocalFireDepartment)
            }
        }
    }
}

@Composable
fun ReadinessRing(percent: Int) {
    val animatedPercent by animateFloatAsState(
        targetValue = percent.toFloat(),
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "readiness"
    )

    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(130.dp)) {
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = radius,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            val sweepAngle = (animatedPercent / 100f) * 360f
            drawArc(
                color = Color(0xFFD97706),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${animatedPercent.toInt()}%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Ready",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun StatChip(value: String, label: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = AccentGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

// ---------- Today's Plan Card ----------

@Composable
fun StudyPlanCard(plan: com.example.learnready.data.db.entities.StudyPlan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (plan.type == "quiz") SoftOrange else SoftBlue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (plan.type == "quiz") Icons.Outlined.Quiz else Icons.Outlined.PlayLesson,
                        contentDescription = plan.type,
                        tint = if (plan.type == "quiz") Orange else PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.titleEn,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (plan.reasonLabel) {
                        "Weak area" -> SoftRed
                        "Review needed" -> SoftGold
                        else -> SoftGreen
                    }
                ) {
                    Text(
                        text = plan.reasonLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = when (plan.reasonLabel) {
                            "Weak area" -> ErrorRed
                            "Review needed" -> Orange
                            else -> SuccessGreen
                        }
                    )
                }
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = "Go",
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ---------- Quiz Performance Chart ----------

@Composable
fun QuizPerformanceChart(scores: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.BarChart,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Quiz Performance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
            }
            Spacer(Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (scores.isEmpty()) return@Canvas

                val barWidth = size.width / (scores.size * 2f)
                val maxHeight = size.height - 20f
                val spacing = barWidth

                scores.forEachIndexed { index, score ->
                    val barHeight = (score / 100f) * maxHeight
                    val x = index * (barWidth + spacing) + spacing / 2

                    val barColor = when {
                        score >= 70 -> Color(0xFF16A34A)
                        score >= 40 -> Color(0xFFD97706)
                        else -> Color(0xFFDC2626)
                    }

                    drawRoundRect(
                        color = barColor.copy(alpha = 0.15f),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, maxHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, maxHeight - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Last ${scores.size} quizzes",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
