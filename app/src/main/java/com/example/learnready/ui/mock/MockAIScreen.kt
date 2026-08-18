package com.example.learnready.ui.mock

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.learnready.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class FaqItem(
    val question: String,
    val answer: String,
    val icon: ImageVector
)

private val faqItems = listOf(
    FaqItem(
        question = "Recommend a study plan",
        answer = "Based on your current progress, I'd recommend:\n\n📅 **Week 1-2:** Focus on completing your pending modules in the current subject.\n\n📅 **Week 3-4:** Take practice quizzes to reinforce your learning.\n\n📅 **Week 5-6:** Explore new subjects and broaden your skill set.\n\n💡 **Tip:** Study for 45 minutes, then take a 15-minute break for optimal retention!",
        icon = Icons.Outlined.CalendarMonth
    ),
    FaqItem(
        question = "What should I learn next?",
        answer = "Looking at your learning journey, here are my suggestions:\n\n🎯 **Immediate:** Complete any in-progress modules first.\n\n📚 **Short-term:** Strengthen subjects where your quiz scores are below 70%.\n\n🚀 **Growth Areas:** Digital Literacy and Communication skills will boost your employability.\n\n⭐ Focus on one subject at a time for deeper understanding!",
        icon = Icons.Outlined.Lightbulb
    ),
    FaqItem(
        question = "How am I performing?",
        answer = "Here's a summary of your performance:\n\n📊 **Overall:** You're making steady progress! Keep up the consistency.\n\n✅ **Strengths:** You show good problem-solving skills based on your quiz patterns.\n\n📈 **Improvement Areas:** Try to score above 80% consistently in quizzes.\n\n🏆 **Tip:** Review incorrect answers after each quiz to learn from mistakes!",
        icon = Icons.Outlined.Analytics
    ),
    FaqItem(
        question = "Help me prepare for interviews",
        answer = "Great initiative! Here's your interview prep guide:\n\n🎤 **Communication:** Practice explaining concepts clearly in both English and Kannada.\n\n💼 **Common Questions:**\n• Tell me about yourself\n• What are your strengths?\n• Why should we hire you?\n\n📝 **Technical Prep:** Review key concepts from your completed modules.\n\n🌟 **Confidence Tip:** Practice with a friend or in front of a mirror. Remember, being genuine is more important than being perfect!",
        icon = Icons.Outlined.Work
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockAIScreen(
    onStartInterview: () -> Unit = {}
) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(true) }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Add welcome message on first load
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages.add(
                ChatMessage(
                    text = "👋 Hello! I'm your AI Learning Assistant.\n\nI can help you with study plans, track your progress, and prepare for interviews. Tap one of the suggestions below or type your question!",
                    isUser = false
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // Header
        Surface(
            shadowElevation = 2.dp,
            color = CardWhite
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = PrimaryBlue
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.SmartToy,
                            contentDescription = "AI",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "AI Learning Assistant",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.labelSmall,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            state = listState,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }

            // Typing indicator
            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }

            // Mock Interview CTA
            if (showSuggestions) {
                item {
                    Card(
                        onClick = onStartInterview,
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DeepBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = CircleShape,
                                color = AccentGold.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Outlined.RecordVoiceOver,
                                        contentDescription = null,
                                        tint = AccentGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Start Mock Interview",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "Practice with 5 random questions",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                Icons.Outlined.ChevronRight,
                                contentDescription = "Start",
                                tint = AccentGold
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            // FAQ suggestions
            if (showSuggestions) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                        Icon(
                            Icons.Outlined.HelpOutline,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Quick Questions",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyBlue
                        )
                    }
                }
                items(faqItems) { faq ->
                    FaqCard(
                        faq = faq,
                        onClick = {
                            showSuggestions = false
                            messages.add(ChatMessage(text = faq.question, isUser = true))
                            isTyping = true

                            scope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                                delay(1500) // Simulate typing
                                isTyping = false
                                messages.add(ChatMessage(text = faq.answer, isUser = false))
                                delay(100)
                                listState.animateScrollToItem(messages.size - 1)
                                delay(500)
                                showSuggestions = true
                            }
                        }
                    )
                }
            }
        }

        // Input field
        Surface(
            shadowElevation = 8.dp,
            color = CardWhite
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Type a message...",
                            color = TextSecondary
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true
                )

                Spacer(Modifier.width(10.dp))

                FilledIconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val text = inputText.trim()
                            inputText = ""
                            messages.add(ChatMessage(text = text, isUser = true))
                            isTyping = true

                            scope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                                delay(1500)
                                isTyping = false
                                messages.add(
                                    ChatMessage(
                                        text = "Thanks for your question! I'm a demo assistant, so I can best help with the suggested topics above. Tap one of the quick questions to see detailed responses! 😊",
                                        isUser = false
                                    )
                                )
                                delay(100)
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Bottom),
                shape = CircleShape,
                color = PrimaryBlue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.SmartToy,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (message.isUser) 18.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 18.dp
            ),
            color = if (message.isUser) PrimaryBlue else SoftBlue,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isUser) Color.White else TextPrimary,
                lineHeight = 22.sp
            )
        }

        if (message.isUser) {
            Spacer(Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Bottom),
                shape = CircleShape,
                color = SoftBlue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "User",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FaqCard(
    faq: FaqItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                color = SoftBlue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        faq.icon,
                        contentDescription = faq.question,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = faq.question,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ask",
                tint = PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier.padding(start = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SoftBlue
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.5f))
                    )
                }
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "AI is typing...",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
