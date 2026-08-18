package com.example.learnready.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import androidx.compose.runtime.remember
import com.example.learnready.ui.onboarding.AuthScreen
import com.example.learnready.ui.onboarding.OnboardingScreen
import com.example.learnready.ui.main.MainTabs
import com.example.learnready.ui.modules.ModuleListScreen
import com.example.learnready.ui.lessons.LessonListScreen
import com.example.learnready.ui.lessons.LessonDetailScreen
import com.example.learnready.ui.quiz.QuizScreen
import com.example.learnready.ui.quiz.QuizResultScreen
import com.example.learnready.ui.skills.SkillModulesScreen
import com.example.learnready.ui.skills.SkillLessonScreen
import com.example.learnready.ui.labs.VirtualLabListScreen
import com.example.learnready.ui.labs.VirtualLabScreen
import com.example.learnready.ui.mock.MockInterviewScreen
import com.example.learnready.ui.profile.SurveyScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object Main : Screen("main")
    object Modules : Screen("modules/{subjectId}") {
        fun createRoute(subjectId: Int) = "modules/$subjectId"
    }
    object Lessons : Screen("lessons/{moduleId}") {
        fun createRoute(moduleId: Int) = "lessons/$moduleId"
    }
    object LessonDetail : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: Int) = "lesson/$lessonId"
    }
    object Quiz : Screen("quiz/{moduleId}") {
        fun createRoute(moduleId: Int) = "quiz/$moduleId"
    }
    object QuizResult : Screen("quiz_result/{score}/{total}/{moduleId}") {
        fun createRoute(score: Int, total: Int, moduleId: Int) = "quiz_result/$score/$total/$moduleId"
    }
    object SkillModules : Screen("skill_modules/{categoryId}") {
        fun createRoute(categoryId: Int) = "skill_modules/$categoryId"
    }
    object SkillLesson : Screen("skill_lesson/{moduleId}") {
        fun createRoute(moduleId: Int) = "skill_lesson/$moduleId"
    }
    object VirtualLabList : Screen("virtual_labs/{subjectId}") {
        fun createRoute(subjectId: Int) = "virtual_labs/$subjectId"
    }
    object VirtualLab : Screen("virtual_lab/{labId}") {
        fun createRoute(labId: Int) = "virtual_lab/$labId"
    }
    object MockInterview : Screen("mock_interview")
    object Survey : Screen("survey")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        enterTransition = { 
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) 
        },
        exitTransition = { 
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) 
        },
        popEnterTransition = { 
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) 
        },
        popExitTransition = { 
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) 
        }
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Main.route) {
            MainTabs(
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.Modules.createRoute(subjectId))
                },
                onSkillCategoryClick = { categoryId ->
                    navController.navigate(Screen.SkillModules.createRoute(categoryId))
                },
                onStartInterview = {
                    navController.navigate(Screen.MockInterview.route)
                },
                onTakeSurvey = {
                    navController.navigate(Screen.Survey.route)
                }
            )
        }

        composable(
            route = Screen.Modules.route,
            arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getInt("subjectId") ?: 0
            ModuleListScreen(
                subjectId = subjectId,
                onBackClick = { navController.popBackStack() },
                onModuleClick = { moduleId ->
                    navController.navigate(Screen.Lessons.createRoute(moduleId))
                },
                onQuizClick = { moduleId ->
                    navController.navigate(Screen.Quiz.createRoute(moduleId))
                },
                onLabsClick = { sId ->
                    navController.navigate(Screen.VirtualLabList.createRoute(sId))
                }
            )
        }

        composable(
            route = Screen.Lessons.route,
            arguments = listOf(navArgument("moduleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getInt("moduleId") ?: 0
            LessonListScreen(
                moduleId = moduleId,
                onBackClick = { navController.popBackStack() },
                onLessonClick = { lessonId ->
                    navController.navigate(Screen.LessonDetail.createRoute(lessonId))
                }
            )
        }

        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0
            LessonDetailScreen(
                lessonId = lessonId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("moduleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getInt("moduleId") ?: 0
            QuizScreen(
                moduleId = moduleId,
                onBackClick = { navController.popBackStack() },
                onQuizComplete = { score, total, mId ->
                    navController.navigate(Screen.QuizResult.createRoute(score, total, mId))
                }
            )
        }

        composable(
            route = Screen.QuizResult.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("moduleId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            val moduleId = backStackEntry.arguments?.getInt("moduleId") ?: 0
            
            val quizEntry: NavBackStackEntry? = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.Quiz.route)
                } catch (e: Exception) {
                    null
                }
            }
            
            val quizViewModel: com.example.learnready.ui.quiz.QuizViewModel? = if (quizEntry != null) {
                androidx.hilt.navigation.compose.hiltViewModel(quizEntry)
            } else null
            
            QuizResultScreen(
                score = score,
                total = total,
                moduleId = moduleId,
                quizViewModel = quizViewModel,
                onBackToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onRetryQuiz = {
                    navController.popBackStack(Screen.Quiz.route, inclusive = false)
                }
            )
        }
        
        composable(
            route = Screen.SkillModules.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            SkillModulesScreen(
                categoryId = categoryId,
                onBackClick = { navController.popBackStack() },
                onModuleClick = { moduleId ->
                    navController.navigate(Screen.SkillLesson.createRoute(moduleId))
                }
            )
        }

        composable(
            route = Screen.SkillLesson.route,
            arguments = listOf(navArgument("moduleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getInt("moduleId") ?: 0
            SkillLessonScreen(
                moduleId = moduleId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.VirtualLabList.route,
            arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getInt("subjectId") ?: 0
            VirtualLabListScreen(
                subjectId = subjectId,
                onBackClick = { navController.popBackStack() },
                onLabClick = { labId ->
                    navController.navigate(Screen.VirtualLab.createRoute(labId))
                }
            )
        }

        composable(
            route = Screen.VirtualLab.route,
            arguments = listOf(navArgument("labId") { type = NavType.IntType })
        ) { backStackEntry ->
            val labId = backStackEntry.arguments?.getInt("labId") ?: 0
            VirtualLabScreen(
                labId = labId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.MockInterview.route) {
            MockInterviewScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Survey.route) {
            SurveyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
