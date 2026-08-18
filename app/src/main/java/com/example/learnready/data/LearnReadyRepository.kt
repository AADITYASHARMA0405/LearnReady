package com.example.learnready.data

import com.example.learnready.data.db.dao.LessonDao
import com.example.learnready.data.db.dao.ModuleDao
import com.example.learnready.data.db.dao.ProgressDao
import com.example.learnready.data.db.dao.QuestionDao
import com.example.learnready.data.db.dao.SkillCategoryDao
import com.example.learnready.data.db.dao.SkillModuleDao
import com.example.learnready.data.db.dao.StudyPlanDao
import com.example.learnready.data.db.dao.VirtualLabDao
import com.example.learnready.data.db.dao.InterviewQuestionDao
import com.example.learnready.data.db.dao.MockInterviewSessionDao
import com.example.learnready.data.db.dao.SurveyResponseDao
import com.example.learnready.data.db.dao.SubjectDao
import com.example.learnready.data.db.dao.UserDao
import com.example.learnready.data.db.entities.Lesson
import com.example.learnready.data.db.entities.Module
import com.example.learnready.data.db.entities.Question
import com.example.learnready.data.db.entities.SkillCategory
import com.example.learnready.data.db.entities.SkillModule
import com.example.learnready.data.db.entities.StudentProgress
import com.example.learnready.data.db.entities.StudyPlan
import com.example.learnready.data.db.entities.Subject
import com.example.learnready.data.db.entities.User
import com.example.learnready.data.db.entities.VirtualLab
import com.example.learnready.data.db.entities.InterviewQuestion
import com.example.learnready.data.db.entities.MockInterviewSession
import com.example.learnready.data.db.entities.SurveyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnReadyRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val moduleDao: ModuleDao,
    private val lessonDao: LessonDao,
    private val questionDao: QuestionDao,
    private val progressDao: ProgressDao,
    private val userDao: UserDao,
    private val studyPlanDao: StudyPlanDao,
    private val skillCategoryDao: SkillCategoryDao,
    private val skillModuleDao: SkillModuleDao,
    private val virtualLabDao: VirtualLabDao,
    private val interviewQuestionDao: InterviewQuestionDao,
    private val mockInterviewSessionDao: MockInterviewSessionDao,
    private val surveyResponseDao: SurveyResponseDao
) {
    // Subject methods
    fun getAllSubjects(): Flow<List<Subject>> = subjectDao.getAllSubjects()

    suspend fun getSubjectById(id: Int): Subject? = subjectDao.getSubjectById(id)

    suspend fun insertAllSubjects(subjects: List<Subject>) = subjectDao.insertAll(subjects)

    // Module methods
    fun getModulesForSubject(subjectId: Int): Flow<List<Module>> =
        moduleDao.getModulesForSubject(subjectId)

    suspend fun getModuleById(id: Int): Module? = moduleDao.getModuleById(id)

    suspend fun insertAllModules(modules: List<Module>) = moduleDao.insertAll(modules)

    // Lesson methods
    fun getLessonsForModule(moduleId: Int): Flow<List<Lesson>> =
        lessonDao.getLessonsForModule(moduleId)

    suspend fun getLessonById(id: Int): Lesson? = lessonDao.getLessonById(id)

    suspend fun insertAllLessons(lessons: List<Lesson>) = lessonDao.insertAll(lessons)

    // Question methods
    fun getQuestionsForModule(moduleId: Int): Flow<List<Question>> =
        questionDao.getQuestionsForModule(moduleId)

    suspend fun insertAllQuestions(questions: List<Question>) = questionDao.insertAll(questions)

    // Progress methods
    fun getProgressForModule(userId: String, moduleId: Int): Flow<List<StudentProgress>> =
        progressDao.getProgressForModule(userId, moduleId)

    fun getAllProgress(userId: String): Flow<List<StudentProgress>> =
        progressDao.getAllProgress(userId)

    suspend fun insertProgress(progress: StudentProgress) =
        progressDao.insertProgress(progress)

    // User methods
    fun getUserFlow(userId: String): Flow<User?> = userDao.getUserFlow(userId)

    suspend fun getUser(userId: String): User? = userDao.getUser(userId)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Study Plan methods
    fun getPendingStudyPlan(userId: String): Flow<List<StudyPlan>> =
        studyPlanDao.getPendingPlan(userId)

    suspend fun insertStudyPlan(plan: StudyPlan) = studyPlanDao.insertPlan(plan)

    suspend fun markPlanCompleted(planId: Int) = studyPlanDao.markCompleted(planId)

    /**
     * Generate a personalized study plan based on student progress.
     * Rules:
     * 1. Modules with low quiz scores (< 70%) → "Weak area"
     * 2. Modules with no progress → "Next in sequence"
     * 3. Modules completed long ago → "Review needed"
     */
    suspend fun generateStudyPlan(userId: String) {
        studyPlanDao.clearPendingPlans(userId)

        val allProgress = getAllProgress(userId).first()
        val allModules = getAllSubjects().first().flatMap { subject ->
            getModulesForSubject(subject.id).first()
        }

        val plans = mutableListOf<StudyPlan>()
        var order = 1

        // Find weak areas: modules with completed quizzes scoring < 70%
        val completedProgress = allProgress.filter { it.status == "completed" && it.score != null }
        val weakModuleIds = completedProgress
            .groupBy { it.moduleId }
            .filter { (_, progresses) ->
                val avgScore = progresses.mapNotNull { it.score }.average()
                avgScore < 70
            }
            .keys

        for (moduleId in weakModuleIds) {
            if (plans.size >= 4) break
            val module = getModuleById(moduleId) ?: continue
            plans.add(
                StudyPlan(
                    userId = userId,
                    moduleId = moduleId,
                    type = "quiz",
                    titleEn = "Retry: ${module.titleEn}",
                    titleKn = module.titleKn,
                    reasonLabel = "Weak area",
                    recommendedOrder = order++
                )
            )
        }

        // Find modules with no progress at all → "Next in sequence"
        val attemptedModuleIds = allProgress.map { it.moduleId }.toSet()
        for (module in allModules) {
            if (plans.size >= 4) break
            if (module.id in attemptedModuleIds) continue

            // Get first lesson for this module
            val lessons = getLessonsForModule(module.id).first()
            val firstLesson = lessons.firstOrNull()

            plans.add(
                StudyPlan(
                    userId = userId,
                    moduleId = module.id,
                    lessonId = firstLesson?.id,
                    type = if (firstLesson != null) "lesson" else "quiz",
                    titleEn = module.titleEn,
                    titleKn = module.titleKn,
                    reasonLabel = "Next in sequence",
                    recommendedOrder = order++
                )
            )
        }

        // If still < 4 items, add review items for old completions
        val oldCompletions = completedProgress
            .filter { it.moduleId !in weakModuleIds }
            .sortedBy { it.updatedAt }
            .distinctBy { it.moduleId }

        for (progress in oldCompletions) {
            if (plans.size >= 4) break
            val module = getModuleById(progress.moduleId) ?: continue
            plans.add(
                StudyPlan(
                    userId = userId,
                    moduleId = progress.moduleId,
                    type = "quiz",
                    titleEn = "Review: ${module.titleEn}",
                    titleKn = module.titleKn,
                    reasonLabel = "Review needed",
                    recommendedOrder = order++
                )
            )
        }

        studyPlanDao.insertAll(plans)
    }

    // Skill Category methods
    fun getAllSkillCategories(): Flow<List<SkillCategory>> = skillCategoryDao.getAllCategories()

    suspend fun getSkillCategoryById(id: Int): SkillCategory? = skillCategoryDao.getCategoryById(id)

    suspend fun insertAllSkillCategories(categories: List<SkillCategory>) = skillCategoryDao.insertAll(categories)

    // Skill Module methods
    fun getSkillModulesForCategory(categoryId: Int): Flow<List<SkillModule>> =
        skillModuleDao.getModulesForCategory(categoryId)

    suspend fun getSkillModuleById(id: Int): SkillModule? = skillModuleDao.getModuleById(id)

    suspend fun insertAllSkillModules(modules: List<SkillModule>) = skillModuleDao.insertAll(modules)

    suspend fun getSkillModuleCountForCategory(categoryId: Int): Int =
        skillModuleDao.getModuleCountForCategory(categoryId)

    // Helper methods
    suspend fun getModuleCountForSubject(subjectId: Int): Int {
        return getModulesForSubject(subjectId).first().size
    }

    suspend fun getLessonCountForSubject(subjectId: Int): Int {
        val modules = getModulesForSubject(subjectId).first()
        var totalLessons = 0
        for (module in modules) {
            totalLessons += getLessonsForModule(module.id).first().size
        }
        return totalLessons
    }

    suspend fun getLessonCountForModule(moduleId: Int): Int {
        return getLessonsForModule(moduleId).first().size
    }

    // Virtual Lab methods
    fun getLabsForSubject(subjectId: Int): Flow<List<VirtualLab>> =
        virtualLabDao.getLabsForSubject(subjectId)

    fun getAllLabs(): Flow<List<VirtualLab>> = virtualLabDao.getAllLabs()

    suspend fun getLabById(id: Int): VirtualLab? = virtualLabDao.getLabById(id)

    suspend fun insertAllLabs(labs: List<VirtualLab>) = virtualLabDao.insertAll(labs)

    // Interview Question methods
    fun getAllInterviewQuestions(): Flow<List<InterviewQuestion>> =
        interviewQuestionDao.getAllQuestions()

    suspend fun getRandomInterviewQuestions(count: Int): List<InterviewQuestion> =
        interviewQuestionDao.getRandomQuestions(count)

    suspend fun insertAllInterviewQuestions(questions: List<InterviewQuestion>) =
        interviewQuestionDao.insertAll(questions)

    // Mock Interview Session methods
    fun getInterviewSessionsForUser(userId: String): Flow<List<MockInterviewSession>> =
        mockInterviewSessionDao.getSessionsForUser(userId)

    suspend fun insertInterviewSession(session: MockInterviewSession): Long =
        mockInterviewSessionDao.insert(session)

    suspend fun updateInterviewSession(session: MockInterviewSession) =
        mockInterviewSessionDao.update(session)

    // Survey Response methods
    fun getSurveyResponsesForUser(userId: String): Flow<List<SurveyResponse>> =
        surveyResponseDao.getResponsesForUser(userId)

    suspend fun insertSurveyResponse(response: SurveyResponse): Long =
        surveyResponseDao.insert(response)
}
