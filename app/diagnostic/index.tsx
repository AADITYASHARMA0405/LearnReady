import React, { useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity, Alert
} from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { QuizOption } from '../../components/ui/QuizOption';
import { useAuth } from '../../hooks/useAuth';
import { execute } from '../../lib/db/database';
import { isKannada } from '../../lib/i18n';

// Static diagnostic questions to gauge general engineering aptitude
const DIAGNOSTIC_QUESTIONS = [
  {
    id: 1,
    topic: 'Math',
    q_en: 'If a train travels 60 km in 45 minutes, what is its speed in km/h?',
    q_kn: 'ಒಂದು ರೈಲು 45 ನಿಮಿಷಗಳಲ್ಲಿ 60 ಕಿ.ಮೀ ಪ್ರಯಾಣಿಸಿದರೆ, ಅದರ ವೇಗ ಕಿ.ಮೀ/ಗಂಟೆಯಲ್ಲಿ ಎಷ್ಟು?',
    options_en: ['80', '90', '75', '60'],
    options_kn: ['80', '90', '75', '60'],
    ans: '80'
  },
  {
    id: 2,
    topic: 'Logic',
    q_en: 'If ALL engineers are smart, and SOME smart people are artists, which is true?',
    q_kn: 'ಎಲ್ಲಾ ಎಂಜಿನಿಯರ್‌ಗಳು ಜಾಣರಾಗಿದ್ದರೆ, ಮತ್ತು ಕೆಲವು ಜಾಣರು ಕಲಾವಿದರಾಗಿದ್ದರೆ, ಯಾವುದು ಸರಿ?',
    options_en: [
      'All artists are engineers',
      'Some engineers are artists',
      'Some artists are smart',
      'No engineers are artists'
    ],
    options_kn: [
      'ಎಲ್ಲಾ ಕಲಾವಿದರು ಎಂಜಿನಿಯರ್‌ಗಳು',
      'ಕೆಲವು ಎಂಜಿನಿಯರ್‌ಗಳು ಕಲಾವಿದರು',
      'ಕೆಲವು ಕಲಾವಿದರು ಜಾಣರು',
      'ಯಾವ ಎಂಜಿನಿಯರ್‌ಗಳೂ ಕಲಾವಿದರಲ್ಲ'
    ],
    ans: 'Some artists are smart'
  },
  {
    id: 3,
    topic: 'Physics',
    q_en: 'Which of the following describes the relationship between Voltage (V), Current (I), and Resistance (R)?',
    q_kn: 'ವೋಲ್ಟೇಜ್ (V), ಕರೆಂಟ್ (I) ಮತ್ತು ಪ್ರತಿರೋಧ (R) ನಡುವಿನ ಸಂಬಂಧವನ್ನು ಈ ಕೆಳಗಿನ ಯಾವುದು ವಿವರಿಸುತ್ತದೆ?',
    options_en: ['V = I / R', 'V = I * R', 'R = V * I', 'I = V * R'],
    options_kn: ['V = I / R', 'V = I * R', 'R = V * I', 'I = V * R'],
    ans: 'V = I * R'
  }
];

export default function DiagnosticScreen() {
  const router = useRouter();
  const { t } = useTranslation();
  const { user, login } = useAuth();
  const kn = isKannada();

  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);
  const [answers, setAnswers] = useState<string[]>([]);
  const [introMode, setIntroMode] = useState(true);

  const currentQ = DIAGNOSTIC_QUESTIONS[currentIndex];

  const handleNext = async () => {
    if (!selectedAnswer) return;

    const newAnswers = [...answers, selectedAnswer];
    setAnswers(newAnswers);

    if (currentIndex < DIAGNOSTIC_QUESTIONS.length - 1) {
      setCurrentIndex(currentIndex + 1);
      setSelectedAnswer(null);
    } else {
      // Calculate score
      let correct = 0;
      DIAGNOSTIC_QUESTIONS.forEach((q, i) => {
        // Simple exact match logic based on english answers for logic simplicity
        // In a real app we'd map options correctly
        if (q.options_en.indexOf(newAnswers[i]) === q.options_en.indexOf(q.ans) || newAnswers[i] === q.ans) {
          correct++;
        }
      });
      const score = Math.round((correct / DIAGNOSTIC_QUESTIONS.length) * 100);

      try {
        if (user) {
          await execute(
            'UPDATE users SET diagnostic_completed = 1, diagnostic_score = ? WHERE id = ?',
            [score, user.id]
          );
          // Update zustand state
          login({ ...user, diagnostic_completed: 1, diagnostic_score: score });
        }
        
        Alert.alert(
          'Assessment Complete!',
          `You scored ${score}%. We've personalized your learning path based on these results.`,
          [{ text: 'Start Learning', onPress: () => router.replace('/(tabs)/home') }]
        );
      } catch (e) {
        console.error('Error saving diagnostic:', e);
        router.replace('/(tabs)/home');
      }
    }
  };

  if (introMode) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.introContent}>
          <View style={styles.iconCircle}>
            <Ionicons name="analytics-outline" size={48} color={Colors.primary} />
          </View>
          <Text style={styles.introTitle}>Quick Assessment</Text>
          <Text style={styles.introText}>
            Let's take a quick 3-question test to gauge your current knowledge level. 
            This helps us personalize your learning recommendations.
          </Text>
          <View style={styles.benefitRow}>
            <Ionicons name="checkmark-circle" size={24} color={Colors.success} />
            <Text style={styles.benefitText}>Takes less than 2 minutes</Text>
          </View>
          <View style={styles.benefitRow}>
            <Ionicons name="checkmark-circle" size={24} color={Colors.success} />
            <Text style={styles.benefitText}>Personalized curriculum</Text>
          </View>
          <View style={styles.benefitRow}>
            <Ionicons name="checkmark-circle" size={24} color={Colors.success} />
            <Text style={styles.benefitText}>No negative marking</Text>
          </View>
          
          <Button 
            title="Start Assessment" 
            onPress={() => setIntroMode(false)}
            style={{ marginTop: Spacing.xxxl, width: '100%' }}
          />
          <TouchableOpacity onPress={() => {
            // Skip logic
            if (user) {
               execute('UPDATE users SET diagnostic_completed = 1 WHERE id = ?', [user.id])
                .then(() => {
                  login({ ...user, diagnostic_completed: 1 });
                  router.replace('/(tabs)/home');
                });
            } else {
              router.replace('/(tabs)/home');
            }
          }} style={styles.skipBtn}>
            <Text style={styles.skipText}>Skip for now</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    );
  }

  const options = kn ? currentQ.options_kn : currentQ.options_en;
  // Fallback map if options don't match EN structurally
  const enOptions = currentQ.options_en;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Assessment</Text>
        <Text style={styles.progressText}>{currentIndex + 1} / {DIAGNOSTIC_QUESTIONS.length}</Text>
      </View>
      
      {/* Progress Bar */}
      <View style={styles.progressBarBg}>
        <View style={[styles.progressBarFill, { width: `${((currentIndex + 1) / DIAGNOSTIC_QUESTIONS.length) * 100}%` }]} />
      </View>

      <ScrollView contentContainerStyle={styles.questionContainer}>
        <View style={styles.topicBadge}>
          <Text style={styles.topicText}>{currentQ.topic}</Text>
        </View>
        
        <Text style={styles.questionText}>
          {kn ? currentQ.q_kn : currentQ.q_en}
        </Text>

        <View style={styles.optionsList}>
          {options.map((opt, i) => (
            <QuizOption
              key={i}
              label={opt}
              index={i}
              selected={selectedAnswer === enOptions[i]}
              state="default"
              onPress={() => setSelectedAnswer(enOptions[i])}
            />
          ))}
        </View>
      </ScrollView>

      <View style={styles.bottomBar}>
        <Button
          title={currentIndex === DIAGNOSTIC_QUESTIONS.length - 1 ? 'Finish' : 'Next'}
          onPress={handleNext}
          disabled={!selectedAnswer}
        />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgMain,
  },
  introContent: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xxxl,
  },
  iconCircle: {
    width: 96,
    height: 96,
    borderRadius: 48,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: Spacing.xl,
  },
  introTitle: {
    ...Typography.headingLG,
    marginBottom: Spacing.md,
    textAlign: 'center',
  },
  introText: {
    ...Typography.body,
    color: Colors.textSecondary,
    textAlign: 'center',
    marginBottom: Spacing.xxl,
    lineHeight: 24,
  },
  benefitRow: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '100%',
    marginBottom: Spacing.md,
    paddingHorizontal: Spacing.xl,
  },
  benefitText: {
    ...Typography.body,
    marginLeft: Spacing.md,
  },
  skipBtn: {
    marginTop: Spacing.lg,
    padding: Spacing.md,
  },
  skipText: {
    ...Typography.bodyBold,
    color: Colors.textHint,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: Spacing.lg,
  },
  headerTitle: {
    ...Typography.headingSM,
  },
  progressText: {
    ...Typography.bodyBold,
    color: Colors.primary,
  },
  progressBarBg: {
    height: 4,
    backgroundColor: Colors.border,
    width: '100%',
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: Colors.primary,
  },
  questionContainer: {
    padding: Spacing.lg,
    paddingTop: Spacing.xl,
  },
  topicBadge: {
    alignSelf: 'flex-start',
    backgroundColor: Colors.bgInput,
    paddingHorizontal: 12,
    paddingVertical: 4,
    borderRadius: BorderRadius.full,
    marginBottom: Spacing.lg,
  },
  topicText: {
    ...Typography.captionBold,
    color: Colors.textSecondary,
  },
  questionText: {
    ...Typography.headingMD,
    lineHeight: 30,
    marginBottom: Spacing.xxl,
  },
  optionsList: {
    gap: Spacing.sm,
  },
  bottomBar: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxl,
    borderTopWidth: 1,
    borderTopColor: Colors.border,
    backgroundColor: Colors.bgMain,
  }
});
