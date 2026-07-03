package com.example.learnready.ui.labs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun VirtualLabScreen(
    labId: Int,
    onBackClick: () -> Unit,
    viewModel: VirtualLabViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        uiState.lab?.titleEn ?: "Virtual Lab",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else if (uiState.lab == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Lab not found", color = TextSecondary)
            }
        } else {
            val allDone = uiState.completedSteps.size == uiState.steps.size && uiState.steps.isNotEmpty()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(BackgroundWhite)
            ) {
                // Progress header
                LabProgressHeader(
                    completed = uiState.completedSteps.size,
                    total = uiState.steps.size,
                    estimatedMinutes = uiState.lab!!.estimatedMinutes
                )

                // Steps list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    uiState.steps.forEachIndexed { index, step ->
                        val isCompleted = index in uiState.completedSteps
                        val isCurrent = index == uiState.currentStepIndex

                        LabStepCard(
                            stepNumber = index + 1,
                            step = step,
                            isCompleted = isCompleted,
                            isCurrent = isCurrent,
                            onToggleComplete = { viewModel.toggleStepComplete(index) },
                            onClick = { viewModel.goToStep(index) }
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    // Completion message
                    AnimatedVisibility(
                        visible = allDone,
                        enter = fadeIn(tween(500)) + expandVertically(tween(500))
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SuccessGreen.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = AccentGold
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    "Lab Complete!",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "You've finished all ${uiState.steps.size} steps. Great work!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = onBackClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SuccessGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Back to Labs")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LabProgressHeader(
    completed: Int,
    total: Int,
    estimatedMinutes: Int
) {
    val progress by animateFloatAsState(
        targetValue = if (total > 0) completed.toFloat() / total else 0f,
        animationSpec = tween(600),
        label = "progress"
    )

    Surface(
        color = DeepBlue,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$completed / $total steps completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardWhite.copy(alpha = 0.9f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "~$estimatedMinutes min",
                        style = MaterialTheme.typography.labelMedium,
                        color = AccentGold
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
}

@Composable
private fun LabStepCard(
    stepNumber: Int,
    step: LabStep,
    isCompleted: Boolean,
    isCurrent: Boolean,
    onToggleComplete: () -> Unit,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isCompleted -> SuccessGreen
            isCurrent -> PrimaryBlue
            else -> Color.Transparent
        },
        animationSpec = tween(300),
        label = "border"
    )

    val bgColor = when {
        isCompleted -> SuccessGreen.copy(alpha = 0.05f)
        isCurrent -> PrimaryBlue.copy(alpha = 0.05f)
        else -> CardWhite
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = if (borderColor != Color.Transparent)
            androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
        else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrent) 3.dp else 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Step number circle
                Surface(
                    shape = CircleShape,
                    color = if (isCompleted) SuccessGreen
                    else if (isCurrent) PrimaryBlue
                    else TextSecondary.copy(alpha = 0.2f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = CardWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                "$stepNumber",
                                color = if (isCurrent) CardWhite else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    step.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCompleted) SuccessGreen else TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                // Expand indicator for current
                if (isCurrent) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Show instruction for current step
            AnimatedVisibility(
                visible = isCurrent,
                enter = fadeIn(tween(300)) + expandVertically(tween(300))
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = PrimaryBlue.copy(alpha = 0.1f))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        step.instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onToggleComplete,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) TextSecondary else SuccessGreen
                        )
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isCompleted) "Undo" else "Mark Complete")
                    }
                }
            }
        }
    }
}
