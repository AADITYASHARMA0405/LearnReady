import React, { useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity, TextInput, KeyboardAvoidingView, Platform,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { CircularProgress } from '../../components/ui/CircularProgress';
import { MOCK_INTERVIEW_QUESTIONS } from '../../lib/db/seed-content';
import { isKannada } from '../../lib/i18n';
import { execute } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';

export default function MockInterviewScreen() {
  const { category } = useLocalSearchParams<{ category: string }>();
  const router = useRouter();
  const { t } = useTranslation();
  const { user } = useAuth();
  const kn = isKannada();

  const questions = MOCK_INTERVIEW_QUESTIONS[category as 'hr' | 'technical'] || [];
  const [currentIndex, setCurrentIndex] = useState(0);
  const [answer, setAnswer] = useState('');
  const [answers, setAnswers] = useState<string[]>([]);
  const [scores, setScores] = useState<number[]>([]);
  const [showFeedback, setShowFeedback] = useState(false);
  const [showResults, setShowResults] = useState(false);

  const currentQ = questions[currentIndex];

  const scoreAnswer = (text: string, keywords: string[]): number => {
    if (!text.trim()) return 0;
    const lower = text.toLowerCase();
    const wordCount = text.split(/\s+/).length;
    let keywordHits = 0;
    keywords.forEach(kw => {
      if (lower.includes(kw.toLowerCase())) keywordHits++;
    });
    const keywordScore = Math.min((keywordHits / keywords.length) * 50, 50);
    const lengthScore = Math.min((wordCount / 30) * 30, 30);
    const structureScore = wordCount >= 10 ? 20 : (wordCount / 10) * 20;
    return Math.round(keywordScore + lengthScore + structureScore);
  };

  const handleSubmitAnswer = () => {
    const score = scoreAnswer(answer, currentQ.keywords);
    setScores([...scores, score]);
    setAnswers([...answers, answer]);
    setShowFeedback(true);
  };

  const handleNext = async () => {
    setShowFeedback(false);
    setAnswer('');
    if (currentIndex < questions.length - 1) {
      setCurrentIndex(currentIndex + 1);
    } else {
      // Save attempt
      if (user) {
        const overallScore = Math.round([...scores].reduce((a, b) => a + b, 0) / scores.length);
        try {
          await execute(
            'INSERT INTO interview_attempts (user_id, category, questions_asked, student_responses, ai_scores, overall_score, attempted_at) VALUES (?, ?, ?, ?, ?, ?, datetime("now"))',
            [user.id, category, JSON.stringify(questions.map(q => q.question_en)), JSON.stringify([...answers]), JSON.stringify([...scores]), overallScore]
          );
        } catch (e) {
          console.error('Error saving interview:', e);
        }
      }
      setShowResults(true);
    }
  };

  // Results screen
  if (showResults) {
    const overallScore = Math.round(scores.reduce((a, b) => a + b, 0) / scores.length);
    return (
      <SafeAreaView style={styles.container}>
        <ScrollView contentContainerStyle={styles.resultsContainer}>
          <Text style={{ fontSize: 48, marginBottom: Spacing.lg }}>
            {overallScore >= 60 ? '🎉' : '💪'}
          </Text>
          <Text style={styles.resultsTitle}>{t('interview.feedback')}</Text>
          <CircularProgress score={overallScore} size={120} />
          <Text style={styles.resultsSubtitle}>{t('interview.overallScore')}</Text>

          {/* Per-question breakdown */}
          <View style={styles.breakdown}>
            {questions.map((q, i) => (
              <View key={i} style={styles.breakdownRow}>
                <Text style={styles.breakdownQ} numberOfLines={1}>
                  Q{i + 1}: {q.question_en}
                </Text>
                <View style={[styles.scoreBadge, { backgroundColor: scores[i] >= 60 ? '#D1FAE5' : '#FEF3C7' }]}>
                  <Text style={[styles.scoreBadgeText, { color: scores[i] >= 60 ? '#059669' : '#D97706' }]}>
                    {scores[i]}%
                  </Text>
                </View>
              </View>
            ))}
          </View>

          <View style={styles.resultsButtons}>
            <Button
              title={t('interview.tryAgain')}
              variant="outline"
              onPress={() => {
                setCurrentIndex(0);
                setAnswer('');
                setAnswers([]);
                setScores([]);
                setShowFeedback(false);
                setShowResults(false);
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

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
          <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{t('interview.title')}</Text>
        <Text style={styles.headerCounter}>
          {currentIndex + 1}/{questions.length}
        </Text>
      </View>

      {/* Progress dots */}
      <View style={styles.dotsRow}>
        {questions.map((_, i) => (
          <View key={i} style={[styles.dot, i === currentIndex && styles.dotActive, i < currentIndex && styles.dotDone]} />
        ))}
      </View>

      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={{ flex: 1 }}
      >
        <ScrollView contentContainerStyle={styles.content}>
          {/* Interviewer bubble */}
          <View style={styles.interviewerRow}>
            <View style={styles.avatar}>
              <Ionicons name="person" size={20} color={Colors.primary} />
            </View>
            <View style={styles.questionBubble}>
              <Text style={styles.questionText}>
                {kn ? currentQ.question_kn : currentQ.question_en}
              </Text>
            </View>
          </View>

          {/* Feedback */}
          {showFeedback && (
            <View style={styles.feedbackCard}>
              <View style={styles.feedbackHeader}>
                <Ionicons
                  name={scores[scores.length - 1] >= 60 ? 'checkmark-circle' : 'alert-circle'}
                  size={20}
                  color={scores[scores.length - 1] >= 60 ? Colors.success : Colors.warning}
                />
                <Text style={styles.feedbackTitle}>
                  Score: {scores[scores.length - 1]}%
                </Text>
              </View>
              <Text style={styles.feedbackText}>
                {scores[scores.length - 1] >= 60
                  ? 'Good answer! You covered the key points well.'
                  : 'Try to include more specific details and examples. Mention key concepts related to the question.'}
              </Text>
              <Text style={styles.feedbackHint}>
                💡 Keywords to include: {currentQ.keywords.slice(0, 3).join(', ')}
              </Text>
            </View>
          )}

          {/* Answer input */}
          {!showFeedback && (
            <View style={styles.answerSection}>
              <TextInput
                style={styles.answerInput}
                multiline
                placeholder={t('interview.typeYourAnswer')}
                placeholderTextColor={Colors.textHint}
                value={answer}
                onChangeText={setAnswer}
                textAlignVertical="top"
              />
            </View>
          )}
        </ScrollView>

        {/* Bottom action */}
        <View style={styles.bottomBar}>
          {!showFeedback ? (
            <Button
              title={t('quiz.checkAnswer')}
              onPress={handleSubmitAnswer}
              disabled={!answer.trim()}
            />
          ) : (
            <Button
              title={currentIndex < questions.length - 1 ? t('interview.nextQuestion') : t('interview.finishInterview')}
              onPress={handleNext}
            />
          )}
        </View>
      </KeyboardAvoidingView>
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
  headerTitle: {
    ...Typography.headingSM,
    flex: 1,
    textAlign: 'center',
  },
  headerCounter: {
    ...Typography.captionBold,
    color: Colors.primary,
    backgroundColor: Colors.primaryLight,
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: BorderRadius.full,
  },
  dotsRow: {
    flexDirection: 'row',
    justifyContent: 'center',
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
  content: {
    padding: Spacing.lg,
    paddingBottom: 120,
  },
  interviewerRow: {
    flexDirection: 'row',
    marginBottom: Spacing.xl,
  },
  avatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: Spacing.md,
  },
  questionBubble: {
    flex: 1,
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    borderTopLeftRadius: 4,
    padding: Spacing.lg,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 1,
  },
  questionText: {
    ...Typography.headingSM,
    lineHeight: 26,
  },
  answerSection: {
    marginTop: Spacing.md,
  },
  answerInput: {
    backgroundColor: Colors.bgCard,
    borderWidth: 1,
    borderColor: Colors.border,
    borderRadius: BorderRadius.md,
    padding: Spacing.lg,
    minHeight: 150,
    ...Typography.body,
    color: Colors.textDark,
  },
  feedbackCard: {
    backgroundColor: '#F0FDF4',
    borderRadius: BorderRadius.md,
    padding: Spacing.lg,
    borderWidth: 1,
    borderColor: '#BBF7D0',
    marginBottom: Spacing.lg,
  },
  feedbackHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
    marginBottom: Spacing.sm,
  },
  feedbackTitle: {
    ...Typography.bodyBold,
    color: Colors.textDark,
  },
  feedbackText: {
    ...Typography.body,
    marginBottom: Spacing.sm,
  },
  feedbackHint: {
    ...Typography.caption,
    color: Colors.textSecondary,
    fontStyle: 'italic',
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
  resultsContainer: {
    flexGrow: 1,
    alignItems: 'center',
    padding: Spacing.xl,
    paddingTop: Spacing.xxxl,
  },
  resultsTitle: {
    ...Typography.headingLG,
    marginBottom: Spacing.xxl,
  },
  resultsSubtitle: {
    ...Typography.body,
    color: Colors.textSecondary,
    marginTop: Spacing.md,
    marginBottom: Spacing.xxl,
  },
  breakdown: {
    width: '100%',
    marginBottom: Spacing.xxl,
  },
  breakdownRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: Spacing.sm,
    borderBottomWidth: 1,
    borderBottomColor: Colors.border,
  },
  breakdownQ: {
    ...Typography.caption,
    flex: 1,
    marginRight: Spacing.sm,
  },
  scoreBadge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: BorderRadius.full,
  },
  scoreBadgeText: {
    ...Typography.captionBold,
  },
  resultsButtons: {
    flexDirection: 'row',
    width: '100%',
  },
});
