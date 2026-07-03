package com.example.learnready.ui.skills

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnready.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SkillsScreen(
    viewModel: SkillsViewModel = hiltViewModel(),
    onCategoryClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Text(
            text = "Skills & Analytics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = NavyBlue
        )
        Text(
            text = "Track your learning progress",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Overall Readiness Score
        ReadinessScoreCard(readiness = uiState.overallReadiness)

        Spacer(Modifier.height(24.dp))

        // Subject Progress
        SectionHeader(title = "Subject Progress", icon = Icons.Outlined.MenuBook)
        Spacer(Modifier.height(12.dp))

        if (uiState.subjectProgress.isEmpty()) {
            EmptyStateCard("Complete quizzes to see subject progress")
        } else {
            Card(
                modifier = Modifier.border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    uiState.subjectProgress.forEachIndexed { index, sp ->
                        SubjectProgressRow(
                            name = sp.subject.nameEn,
                            percentage = sp.progressPercent,
                            color = getSubjectColor(index)
                        )
                        if (index < uiState.subjectProgress.size - 1) {
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Employability Skills Categories
        SectionHeader(title = "Employability Skills", icon = Icons.Outlined.Psychology)
        Spacer(Modifier.height(12.dp))

        if (uiState.skillCategories.isEmpty()) {
            EmptyStateCard("No skill categories found")
        } else {
            Card(
                modifier = Modifier.border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    uiState.skillCategories.forEachIndexed { index, category ->
                        CategoryRow(
                            name = category.nameEn,
                            colorHex = category.color,
                            onClick = { onCategoryClick(category.id) }
                        )
                        if (index < uiState.skillCategories.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = SurfaceBorder
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Recent Activity
        SectionHeader(title = "Recent Activity", icon = Icons.Outlined.History)
        Spacer(Modifier.height(12.dp))

        if (uiState.recentActivity.isEmpty()) {
            EmptyStateCard("No recent activity yet. Start learning!")
        } else {
            Card(
                modifier = Modifier.border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    uiState.recentActivity.forEachIndexed { index, activity ->
                        ActivityRow(activity = activity)
                        if (index < uiState.recentActivity.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = SurfaceBorder
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ReadinessScoreCard(readiness: Int) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular progress
            val animatedProgress by animateFloatAsState(
                targetValue = readiness.toFloat(),
                animationSpec = tween(1500, easing = FastOutSlowInEasing),
                label = "readinessAnim"
            )

            val progressColor = when {
                readiness >= 70 -> SuccessGreen
                readiness >= 40 -> Orange
                else -> Color(0xFFEF4444)
            }

            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(120.dp)) {
                    drawCircle(
                        color = SurfaceBorder,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = progressColor,
                        startAngle = -90f,
                        sweepAngle = (animatedProgress / 100f) * 360f,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${animatedProgress.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = progressColor
                    )
                    Text(
                        text = "Ready",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.width(24.dp))

            Column {
                Text(
                    text = "Overall Readiness",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = when {
                        readiness >= 70 -> "Great progress! You're well prepared."
                        readiness >= 40 -> "Good start! Keep learning to improve."
                        readiness > 0 -> "Just getting started. Keep going!"
                        else -> "Start learning to build your readiness score!"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = title,
            tint = PrimaryBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
private fun CategoryRow(
    name: String,
    colorHex: String,
    onClick: () -> Unit
) {
    val parseColor = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        PrimaryBlue
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = parseColor.copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.School,
                        contentDescription = name,
                        tint = parseColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = "Go",
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SubjectProgressRow(
    name: String,
    percentage: Int,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progressAnim"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f),
        )
    }
}

@Composable
private fun ActivityRow(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = if (activity.type == "quiz") SoftBlue else SoftGreen
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (activity.type == "quiz") Icons.Outlined.Quiz else Icons.Outlined.CheckCircle,
                    contentDescription = activity.type,
                    tint = if (activity.type == "quiz") PrimaryBlue else SuccessGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = formatTimestamp(activity.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = "Info",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

private fun getSubjectColor(index: Int): Color {
    val colors = listOf(PrimaryBlue, SuccessGreen, Purple, Orange, Color(0xFFEF4444), Color(0xFFF59E0B))
    return colors[index % colors.size]
}

private fun formatTimestamp(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        "Unknown"
    }
}
