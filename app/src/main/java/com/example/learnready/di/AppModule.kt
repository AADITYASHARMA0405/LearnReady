package com.example.learnready.di

import android.content.Context
import androidx.room.Room
import com.example.learnready.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "learnready.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Provides
    fun provideSubjectDao(db: AppDatabase) = db.subjectDao()

    @Provides
    fun provideModuleDao(db: AppDatabase) = db.moduleDao()

    @Provides
    fun provideLessonDao(db: AppDatabase) = db.lessonDao()

    @Provides
    fun provideQuestionDao(db: AppDatabase) = db.questionDao()

    @Provides
    fun provideProgressDao(db: AppDatabase) = db.progressDao()

    @Provides
    fun provideStudyPlanDao(db: AppDatabase) = db.studyPlanDao()

    @Provides
    fun provideSkillCategoryDao(db: AppDatabase) = db.skillCategoryDao()

    @Provides
    fun provideSkillModuleDao(db: AppDatabase) = db.skillModuleDao()

    @Provides
    fun provideVirtualLabDao(db: AppDatabase) = db.virtualLabDao()

    @Provides
    fun provideInterviewQuestionDao(db: AppDatabase) = db.interviewQuestionDao()

    @Provides
    fun provideMockInterviewSessionDao(db: AppDatabase) = db.mockInterviewSessionDao()

    @Provides
    fun provideSurveyResponseDao(db: AppDatabase) = db.surveyResponseDao()
}
