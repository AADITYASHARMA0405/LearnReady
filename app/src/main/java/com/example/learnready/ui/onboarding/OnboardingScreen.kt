package com.example.learnready.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.learnready.R
import com.example.learnready.ui.components.PrimaryButton
import com.example.learnready.ui.theme.*

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int,
    val backgroundColor: Color
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "A better way to learn",
            description = "Master engineering subjects with comprehensive, interactive lessons designed for you.",
            imageRes = R.drawable.onboarding_lesson_1780340270493,
            backgroundColor = BackgroundWhite
        ),
        OnboardingPage(
            title = "Interactive Curriculum",
            description = "Track your progress, unlock achievements, and excel in your exams.",
            imageRes = R.drawable.onboarding_curriculum_1780340223283,
            backgroundColor = BackgroundWhite
        ),
        OnboardingPage(
            title = "Learn at your own pace",
            description = "No rush. Review your past mistakes and retake quizzes until you get a perfect score.",
            imageRes = R.drawable.onboarding_atmosphere_1780340246875,
            backgroundColor = BackgroundWhite
        ),
        OnboardingPage(
            title = "Join a community of learners",
            description = "Connect with peers, share knowledge, and learn together.",
            imageRes = R.drawable.onboarding_boy_student_1780340383466,
            backgroundColor = BackgroundWhite
        )
    )

    // Using a simple state for demonstration. In a real app we'd use HorizontalPager from Accompanist or Foundation.
    val currentPageIndex = androidx.compose.runtime.remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val currentPage = pages[currentPageIndex.intValue]

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = currentPage.backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Image
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = currentPage.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
            
            // Text Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentPage.title,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentPage.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Indicators
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (index == currentPageIndex.intValue) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentPageIndex.intValue) 
                                        PrimaryBlue 
                                    else 
                                        SurfaceBorder
                                )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                PrimaryButton(
                    text = if (currentPageIndex.intValue == pages.lastIndex) "Get Started" else "Next",
                    onClick = {
                        if (currentPageIndex.intValue < pages.lastIndex) {
                            currentPageIndex.intValue++
                        } else {
                            onFinish()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
