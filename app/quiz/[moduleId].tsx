import React, { useEffect, useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity, ActivityIndicator,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { QuizOption } from '../../components/ui/QuizOption';
import { CircularProgress } from '../../components/ui/CircularProgress';
import { queryAll, execute } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';
import { isKannada } from '../../lib/i18n';

interface Question {
  id: number;
  question_en: string;
  question_kn: string;
  options_en: string;
  options_kn: string;
  correct_answer: string;
  explanation_en: string;
  explanation_kn: string;
}

type AnswerState = 'default' | 'correct' | 'wrong';

export default function QuizScreen() {
  const { moduleId } = useLocalSearchParams<{ moduleId: string }>();
  const router = useRouter();
  const { t } = useTranslation();
  const { user } = useAuth();
  const [questions, setQuestions] = useState<Question[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);
  const [answerState, setAnswerState] = useState<AnswerState>('default');
  const [score, setScore] = useState(0);
  const [showResults, setShowResults] = useState(false);
  const [loading, setLoading] = useState(true);
  const [answeredQuestions, setAnsweredQuestions] = useState<boolean[]>([]);

  useEffect(() => {
    loadQuestions();
  }, [moduleId]);

  const loadQuestions = async () => {
    try {
      const data = await queryAll<Question>(
        'SELECT * FROM questions WHERE module_id = ? ORDER BY id', [Number(moduleId)]
      );
      setQuestions(data);
      setAnsweredQuestions(new Array(data.length).fill(false));
    } catch (error) {
      console.error('Error loading questions:', error);
    } finally {
      setLoading(false);
    }
  };

  const currentQuestion = questions[currentIndex];
  const kn = isKannada();

  const getQuestionText = () => kn ? (currentQuestion?.question_kn || currentQuestion?.question_en) : currentQuestion?.question_en;
  const getOptions = (): string[] => {
    try {
      const raw = kn ? (currentQuestion?.options_kn || currentQuestion?.options_en) : currentQuestion?.options_en;
      return JSON.parse(raw || '[]');
    } catch { return []; }
  };
  const getExplanation = () => kn ? (currentQuestion?.explanation_kn || currentQuestion?.explanation_en) : currentQuestion?.explanation_en;

  const handleSelectAnswer = (option: string) => {
    if (answerState !== 'default') return;
    setSelectedAnswer(option);
  };

  const handleCheckAnswer = async () => {
    if (!selectedAnswer || !currentQuestion) return;

    const isCorrect = selectedAnswer === currentQuestion.correct_answer;
    setAnswerState(isCorrect ? 'correct' : 'wrong');

    if (isCorrect) {
      setScore(score + 1);
    }

    // Record attempt
    if (user) {
      try {
        await execute(
          'INSERT INTO quiz_attempts (user_id, question_id, selected_answer, is_correct, attempted_at) VALUES (?, ?, ?, ?, datetime("now"))',
          [user.id, currentQuestion.id, selectedAnswer, isCorrect ? 1 : 0]
        );
      } catch (e) {
        console.error('Error recording attempt:', e);
      }
    }

    const newAnswered = [...answeredQuestions];
    newAnswered[currentIndex] = true;
    setAnsweredQuestions(newAnswered);
  };

  const handleNext = async () => {
    if (currentIndex < questions.length - 1) {
      setCurrentIndex(currentIndex + 1);
      setSelectedAnswer(null);
      setAnswerState('default');
    } else {
      // Quiz complete — update progress
      if (user) {
        const percentage = Math.round((score / questions.length) * 100);
        try {
          await execute(`
            UPDATE student_progress 
            SET status = 'completed', 
                score = ?, 
                best_score = MAX(COALESCE(best_score, 0), ?),
                attempts = attempts + 1,
                completed_at = datetime('now')
            WHERE user_id = ? AND module_id = ?
          `, [percentage, percentage, user.id, Number(moduleId)]);
        } catch (e) {
          console.error('Error updating progress:', e);
        }
      }
      setShowResults(true);
    }
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <ActivityIndicator size="large" color={Colors.primary} style={{ flex: 1 }} />
      </SafeAreaView>
    );
  }

  if (questions.length === 0) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity onPress={() => router.back()}>
            <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
          </TouchableOpacity>
        </View>
        <View style={styles.emptyState}>
          <Ionicons name="help-circle-outline" size={64} color={Colors.textHint} />
          <Text style={styles.emptyText}>No questions yet</Text>
        </View>
      </SafeAreaView>
    );
  }

  // Results screen
  if (showResults) {
    const percentage = Math.round((score / questions.length) * 100);
    const isGood = percentage >= 60;

    return (
      <SafeAreaView style={styles.container}>
        <ScrollView contentContainerStyle={styles.resultsContainer}>
          <View style={styles.resultsEmoji}>
            <Text style={{ fontSize: 48 }}>{isGood ? '🎉' : '💪'}</Text>
          </View>
          <Text style={styles.resultsTitle}>{t('quiz.quizComplete')}</Text>
          <CircularProgress score={percentage} size={120} />
          <Text style={styles.resultsScore}>
            {score} / {questions.length} {t('common.correct')}
          </Text>
          <Text style={styles.resultsMessage}>
            {isGood
              ? 'Great job! You have a good understanding of this topic.'
              : 'Keep practicing! Review the lessons and try again.'}
          </Text>
          <View style={styles.resultsButtons}>
            <Button
              title={t('quiz.tryAgain')}
              variant="outline"
              onPress={() => {
                setCurrentIndex(0);
                setSelectedAnswer(null);
                setAnswerState('default');
                setScore(0);
                setShowResults(false);
                setAnsweredQuestions(new Array(questions.length).fill(false));
              }}
              style={{ flex: 1, marginRight: Spacing.sm }}
            />
            <Button
              title={t('common.done')}
              onPress={() => router.back()}
              style={{ flex: 1 }}
            />
          </View>
        </ScrollView>
      </SafeAreaView>
    );
  }

  const options = getOptions();

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
          <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
        </TouchableOpacity>
        <View style={styles.headerCenter}>
          <Text style={styles.headerLabel}>
            {t('quiz.question', { number: currentIndex + 1 })} {t('quiz.of')} {questions.length}
          </Text>
        </View>
        <View style={{ width: 40 }} />
      </View>

      {/* Progress dots */}
      <View style={styles.dotsRow}>
        {questions.map((_, i) => (
          <View
            key={i}
            style={[
              styles.dot,
              i === currentIndex && styles.dotActive,
              answeredQuestions[i] && styles.dotDone,
            ]}
          />
        ))}
      </View>

      {/* Question */}
      <ScrollView contentContainerStyle={styles.questionContainer}>
        <Text style={styles.questionText}>{getQuestionText()}</Text>

        {/* Options */}
        <View style={styles.optionsContainer}>
          {options.map((option, i) => {
            let state: 'default' | 'correct' | 'wrong' = 'default';
            if (answerState !== 'default') {
              if (option === currentQuestion.correct_answer) state = 'correct';
              else if (option === selectedAnswer) state = 'wrong';
            }

            return (
              <QuizOption
                key={i}
                label={option}
                index={i}
                selected={selectedAnswer === option}
                state={state}
                onPress={() => handleSelectAnswer(option)}
                disabled={answerState !== 'default'}
              />
            );
          })}
        </View>

        {/* Explanation */}
        {answerState !== 'default' && (
          <View style={[styles.explanationCard, answerState === 'correct' ? styles.explanationCorrect : styles.explanationWrong]}>
            <View style={styles.explanationHeader}>
              <Ionicons
                name={answerState === 'correct' ? 'checkmark-circle' : 'close-circle'}
                size={20}
                color={answerState === 'correct' ? Colors.success : Colors.error}
              />
              <Text style={[styles.explanationTitle, { color: answerState === 'correct' ? Colors.success : Colors.error }]}>
                {answerState === 'correct' ? t('quiz.correctFeedback') : t('quiz.wrongFeedback')}
              </Text>
            </View>
            {answerState === 'wrong' && (
              <Text style={styles.correctAnswerText}>
                ✓ {currentQuestion.correct_answer}
              </Text>
            )}
            <Text style={styles.explanationText}>{getExplanation()}</Text>
          </View>
        )}
      </ScrollView>

      {/* Bottom Action */}
      <View style={styles.bottomBar}>
        {answerState === 'default' ? (
          <Button
            title={t('quiz.checkAnswer')}
            onPress={handleCheckAnswer}
            disabled={!selectedAnswer}
          />
        ) : (
          <Button
            title={currentIndex < questions.length - 1 ? t('quiz.nextQuestion') : t('common.done')}
            onPress={handleNext}
          />
        )}
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgMain,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
  },
  backBtn: {
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },
  headerCenter: {
    flex: 1,
    alignItems: 'center',
  },
  headerLabel: {
    ...Typography.bodyBold,
    color: Colors.textSecondary,
  },
  dotsRow: {
    flexDirection: 'row',
    justifyContent: 'center',
    paddingHorizontal: Spacing.lg,
    paddingBottom: Spacing.md,
    gap: 6,
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: Colors.border,
  },
  dotActive: {
    backgroundColor: Colors.primary,
    width: 20,
  },
  dotDone: {
    backgroundColor: Colors.success,
  },
  questionContainer: {
    padding: Spacing.lg,
    paddingBottom: 120,
  },
  questionText: {
    ...Typography.headingMD,
    marginBottom: Spacing.xxl,
    lineHeight: 30,
  },
  optionsContainer: {
    marginBottom: Spacing.lg,
  },
  explanationCard: {
    borderRadius: BorderRadius.md,
    padding: Spacing.lg,
    marginTop: Spacing.sm,
  },
  explanationCorrect: {
    backgroundColor: '#F0FDF4',
    borderWidth: 1,
    borderColor: '#BBF7D0',
  },
  explanationWrong: {
    backgroundColor: '#FEF2F2',
    borderWidth: 1,
    borderColor: '#FECACA',
  },
  explanationHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
    marginBottom: Spacing.sm,
  },
  explanationTitle: {
    ...Typography.bodyBold,
  },
  correctAnswerText: {
    ...Typography.bodyBold,
    color: Colors.success,
    marginBottom: Spacing.sm,
  },
  explanationText: {
    ...Typography.body,
    color: Colors.textPrimary,
    lineHeight: 22,
  },
  bottomBar: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    padding: Spacing.lg,
    paddingBottom: Spacing.xxl,
    backgroundColor: Colors.bgMain,
    borderTopWidth: 1,
    borderTopColor: Colors.border,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  emptyText: {
    ...Typography.headingMD,
    color: Colors.textHint,
    marginTop: Spacing.lg,
  },
  resultsContainer: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xxxl,
  },
  resultsEmoji: {
    marginBottom: Spacing.lg,
  },
  resultsTitle: {
    ...Typography.headingLG,
    marginBottom: Spacing.xxl,
  },
  resultsScore: {
    ...Typography.headingMD,
    color: Colors.textSecondary,
    marginTop: Spacing.xl,
  },
  resultsMessage: {
    ...Typography.body,
    color: Colors.textSecondary,
    textAlign: 'center',
    marginTop: Spacing.md,
    marginBottom: Spacing.xxxl,
  },
  resultsButtons: {
    flexDirection: 'row',
    width: '100%',
  },
});
