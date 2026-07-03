package com.example.learnready.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learnready.data.db.dao.LessonDao
import com.example.learnready.data.db.dao.ModuleDao
import com.example.learnready.data.db.dao.ProgressDao
import com.example.learnready.data.db.dao.QuestionDao
import com.example.learnready.data.db.dao.SkillCategoryDao
import com.example.learnready.data.db.dao.SkillModuleDao
import com.example.learnready.data.db.dao.StudyPlanDao
import com.example.learnready.data.db.dao.SubjectDao
import com.example.learnready.data.db.dao.UserDao
import com.example.learnready.data.db.dao.VirtualLabDao
import com.example.learnready.data.db.dao.InterviewQuestionDao
import com.example.learnready.data.db.dao.MockInterviewSessionDao
import com.example.learnready.data.db.dao.SurveyResponseDao
import com.example.learnready.data.db.entities.InterviewQuestion
import com.example.learnready.data.db.entities.Lesson
import com.example.learnready.data.db.entities.MockInterviewSession
import com.example.learnready.data.db.entities.SurveyResponse
import com.example.learnready.data.db.entities.Module
import com.example.learnready.data.db.entities.Question
import com.example.learnready.data.db.entities.SkillCategory
import com.example.learnready.data.db.entities.SkillModule
import com.example.learnready.data.db.entities.StudentProgress
import com.example.learnready.data.db.entities.StudyPlan
import com.example.learnready.data.db.entities.Subject
import com.example.learnready.data.db.entities.User
import com.example.learnready.data.db.entities.VirtualLab

@Database(
    entities = [
        User::class,
        Subject::class,
        Module::class,
        Lesson::class,
        Question::class,
        StudentProgress::class,
        StudyPlan::class,
        SkillCategory::class,
        SkillModule::class,
        VirtualLab::class,
        InterviewQuestion::class,
        MockInterviewSession::class,
        SurveyResponse::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun moduleDao(): ModuleDao
    abstract fun lessonDao(): LessonDao
    abstract fun questionDao(): QuestionDao
    abstract fun progressDao(): ProgressDao
    abstract fun studyPlanDao(): StudyPlanDao
    abstract fun skillCategoryDao(): SkillCategoryDao
    abstract fun skillModuleDao(): SkillModuleDao
    abstract fun virtualLabDao(): VirtualLabDao
    abstract fun interviewQuestionDao(): InterviewQuestionDao
    abstract fun mockInterviewSessionDao(): MockInterviewSessionDao
    abstract fun surveyResponseDao(): SurveyResponseDao
}
