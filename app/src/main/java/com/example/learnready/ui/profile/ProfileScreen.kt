package com.example.learnready.ui.profile

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnready.ui.theme.*

@Composable
fun ProfileScreen(
    onTakeSurvey: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header with gradient
        ProfileHeader(
            userName = uiState.user?.fullName ?: "Demo User",
            phone = uiState.user?.phone ?: "+91 XXXXXXXXXX"
        )

        // Stats row
        StatsRow(
            modulesCompleted = uiState.modulesCompleted,
            averageScore = uiState.averageScore,
            subjectsCount = uiState.subjectsCount
        )

        // Achievement badges
        AchievementSection(badges = uiState.badges)

        // Settings section
        SettingsSection(
            selectedLanguage = uiState.selectedLanguage,
            onTakeSurvey = onTakeSurvey,
            onLanguageToggle = viewModel::toggleLanguage
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    phone: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryBlue, PrimaryBlue.copy(alpha = 0.8f))
                )
            )
            .padding(top = 48.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Avatar
            Surface(
                modifier = Modifier.size(96.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.size(88.dp),
                        shape = CircleShape,
                        color = SoftBlue
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Profile",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = phone,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun StatsRow(
    modulesCompleted: Int,
    averageScore: Int,
    subjectsCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-24).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.CheckCircle,
            value = "$modulesCompleted",
            label = "Modules",
            color = SuccessGreen,
            bgColor = SoftGreen
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Star,
            value = "$averageScore%",
            label = "Avg Score",
            color = Orange,
            bgColor = SoftOrange
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.MenuBook,
            value = "$subjectsCount",
            label = "Subjects",
            color = Purple,
            bgColor = SoftPurple
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    bgColor: Color
) {
    Card(
        modifier = modifier.clickable {  }.border(1.dp, SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun AchievementSection(badges: List<Badge>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            badges.forEach { badge ->
                BadgeCard(
                    badge = badge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: Badge,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {  }.border(1.dp, if (badge.isEarned) SurfaceBorder.copy(alpha=0.5f) else Color.Transparent, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isEarned) CardWhite else Color(0xFFF1F5F9)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (badge.isEarned) 2.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconVector = when(badge.icon) {
                BadgeIcon.QUIZ -> Icons.Outlined.Assignment
                BadgeIcon.PERFECT -> Icons.Outlined.WorkspacePremium
                BadgeIcon.LESSONS -> Icons.Outlined.AutoStories
                BadgeIcon.QUICK -> Icons.Outlined.Bolt
            }
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = if (badge.isEarned) AccentGold else TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(36.dp)
                    .padding(bottom = 6.dp)
            )
            Text(
                text = badge.title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (badge.isEarned) TextPrimary else TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SettingsSection(
    selectedLanguage: String,
    onTakeSurvey: () -> Unit = {},
    onLanguageToggle: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Settings & Feedback",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Survey CTA
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AccentGold.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            onClick = onTakeSurvey
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = AccentGold.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.Feedback,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Take Feedback Survey",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Help us improve the app",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Language toggle
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                SettingsRow(
                    icon = Icons.Outlined.Language,
                    title = "Language",
                    subtitle = if (selectedLanguage == "en") "English" else "ಕನ್ನಡ (Kannada)",
                    iconColor = PrimaryBlue,
                    onClick = onLanguageToggle
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = SurfaceBorder                )

                SettingsRow(
                    icon = Icons.Outlined.Info,
                    title = "About App",
                    subtitle = "LearnReady – Empowering Rural Learners",
                    iconColor = Purple
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = SurfaceBorder
                )

                SettingsRow(
                    icon = Icons.Outlined.Build,
                    title = "Version",
                    subtitle = "1.0.0 (Build 1)",
                    iconColor = Orange
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = iconColor.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = "Navigate",
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
