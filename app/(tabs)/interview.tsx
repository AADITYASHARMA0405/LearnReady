import React from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity,
} from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { LanguageToggle } from '../../components/ui/LanguageToggle';

export default function InterviewScreen() {
  const router = useRouter();
  const { t } = useTranslation();

  const categories = [
    {
      id: 'hr',
      title: t('interview.hrInterview'),
      description: t('interview.hrDesc'),
      icon: 'people-outline' as const,
      color: '#8B5CF6',
      bgColor: '#EDE9FE',
      questionCount: 5,
    },
    {
      id: 'technical',
      title: t('interview.technicalInterview'),
      description: t('interview.technicalDesc'),
      icon: 'code-slash-outline' as const,
      color: '#3B82F6',
      bgColor: '#DBEAFE',
      questionCount: 5,
    },
  ];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.title}>{t('interview.title')}</Text>
            <Text style={styles.subtitle}>{t('interview.subtitle')}</Text>
          </View>
          <LanguageToggle />
        </View>

        {/* Hero illustration area */}
        <View style={styles.heroCard}>
          <View style={styles.heroIconRow}>
            <View style={[styles.heroIcon, { backgroundColor: '#EDE9FE' }]}>
              <Ionicons name="mic-outline" size={28} color="#8B5CF6" />
            </View>
            <View style={[styles.heroIcon, { backgroundColor: '#DBEAFE' }]}>
              <Ionicons name="chatbubble-ellipses-outline" size={28} color="#3B82F6" />
            </View>
            <View style={[styles.heroIcon, { backgroundColor: '#D1FAE5' }]}>
              <Ionicons name="checkmark-done-outline" size={28} color="#10B981" />
            </View>
          </View>
          <Text style={styles.heroTitle}>Practice makes perfect!</Text>
          <Text style={styles.heroDesc}>
            Answer interview questions and get instant feedback on your responses.
          </Text>
        </View>

        {/* Interview Type Cards */}
        {categories.map((cat) => (
          <TouchableOpacity
            key={cat.id}
            style={styles.interviewCard}
            onPress={() => router.push(`/mock-interview/${cat.id}` as any)}
            activeOpacity={0.7}
          >
            <View style={[styles.cardIcon, { backgroundColor: cat.bgColor }]}>
              <Ionicons name={cat.icon} size={32} color={cat.color} />
            </View>
            <View style={styles.cardContent}>
              <Text style={styles.cardTitle}>{cat.title}</Text>
              <Text style={styles.cardDesc} numberOfLines={2}>{cat.description}</Text>
              <View style={styles.cardMeta}>
                <Ionicons name="help-circle-outline" size={14} color={Colors.textHint} />
                <Text style={styles.cardMetaText}>{cat.questionCount} questions</Text>
                <Ionicons name="time-outline" size={14} color={Colors.textHint} style={{ marginLeft: Spacing.md }} />
                <Text style={styles.cardMetaText}>~10 min</Text>
              </View>
            </View>
            <View style={[styles.startBadge, { backgroundColor: cat.bgColor }]}>
              <Ionicons name="play" size={16} color={cat.color} />
            </View>
          </TouchableOpacity>
        ))}

        {/* Tips */}
        <View style={styles.tipsSection}>
          <Text style={styles.tipsTitle}>💡 Interview Tips</Text>
          {[
            'Be honest and give specific examples',
            'Keep your answers concise (1-2 minutes)',
            'Show enthusiasm and confidence',
            'Ask questions about the company',
          ].map((tip, i) => (
            <View key={i} style={styles.tipRow}>
              <Text style={styles.tipBullet}>•</Text>
              <Text style={styles.tipText}>{tip}</Text>
            </View>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgMain,
  },
  scrollContent: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxxl,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: Spacing.xl,
    paddingTop: Spacing.md,
  },
  title: {
    ...Typography.headingLG,
  },
  subtitle: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginTop: 2,
  },
  heroCard: {
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.xl,
    alignItems: 'center',
    marginBottom: Spacing.xl,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  heroIconRow: {
    flexDirection: 'row',
    gap: Spacing.md,
    marginBottom: Spacing.lg,
  },
  heroIcon: {
    width: 56,
    height: 56,
    borderRadius: 28,
    justifyContent: 'center',
    alignItems: 'center',
  },
  heroTitle: {
    ...Typography.headingMD,
    marginBottom: Spacing.xs,
  },
  heroDesc: {
    ...Typography.body,
    color: Colors.textSecondary,
    textAlign: 'center',
  },
  interviewCard: {
    flexDirection: 'row',
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    marginBottom: Spacing.md,
    alignItems: 'center',
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 1,
  },
  cardIcon: {
    width: 56,
    height: 56,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: Spacing.md,
  },
  cardContent: {
    flex: 1,
  },
  cardTitle: {
    ...Typography.headingSM,
    marginBottom: 2,
  },
  cardDesc: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginBottom: Spacing.sm,
  },
  cardMeta: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  cardMetaText: {
    ...Typography.caption,
    color: Colors.textHint,
    fontSize: 11,
  },
  startBadge: {
    width: 36,
    height: 36,
    borderRadius: 18,
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: Spacing.sm,
  },
  tipsSection: {
    backgroundColor: '#FFFBEB',
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    marginTop: Spacing.md,
    borderWidth: 1,
    borderColor: '#FDE68A',
  },
  tipsTitle: {
    ...Typography.headingSM,
    marginBottom: Spacing.md,
  },
  tipRow: {
    flexDirection: 'row',
    marginBottom: Spacing.xs,
  },
  tipBullet: {
    color: Colors.warning,
    marginRight: Spacing.sm,
    fontWeight: '700',
  },
  tipText: {
    ...Typography.body,
    flex: 1,
    color: Colors.textPrimary,
  },
});
