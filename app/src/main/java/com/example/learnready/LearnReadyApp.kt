package com.example.learnready

import android.app.Application
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.User
import com.example.learnready.data.db.seed.SeedData
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LearnReadyApp : Application() {

    @Inject
    lateinit var repository: LearnReadyRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        seedDatabase()
    }

    private fun seedDatabase() {
        applicationScope.launch {
            // Only seed if no subjects exist yet
            val existingUser = repository.getUser("demo_user")
            if (existingUser == null) {
                repository.insertAllSubjects(SeedData.subjects)
                repository.insertAllModules(SeedData.modules)
                repository.insertAllLessons(SeedData.lessons)
                repository.insertAllQuestions(SeedData.questions)
                repository.insertAllSkillCategories(SeedData.skillCategories)
                repository.insertAllSkillModules(SeedData.skillModules)
                repository.insertAllLabs(SeedData.virtualLabs)
                repository.insertAllInterviewQuestions(SeedData.interviewQuestions)
                repository.insertUser(
                    User(
                        id = "demo_user",
                        phone = "+91 9876543210",
                        fullName = "Aaditya",
                        selectedLanguage = "en"
                    )
                )
            }
        }
    }
}
