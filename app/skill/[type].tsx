import React, { useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';
import { SEED_SKILL_MODULES } from '../../lib/db/seed-content';
import { isKannada } from '../../lib/i18n';

export default function SkillDetailScreen() {
  const { type } = useLocalSearchParams<{ type: string }>();
  const router = useRouter();
  const { t } = useTranslation();
  const [currentLessonIndex, setCurrentLessonIndex] = useState<number | null>(null);
  const kn = isKannada();

  const skillModule = SEED_SKILL_MODULES.find(s => s.type === type);

  if (!skillModule) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>Module not found</Text>
      </SafeAreaView>
    );
  }

  const title = kn ? skillModule.title_kn : skillModule.title_en;

  // Show lesson content
  if (currentLessonIndex !== null) {
    const lesson = skillModule.lessons[currentLessonIndex];
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity onPress={() => setCurrentLessonIndex(null)} style={styles.backBtn}>
            <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
          </TouchableOpacity>
          <Text style={styles.headerTitle} numberOfLines={1}>
            {kn ? lesson.title_kn : lesson.title_en}
          </Text>
          <View style={{ width: 40 }} />
        </View>
        <ScrollView contentContainerStyle={styles.lessonContent}>
          <Text style={styles.lessonTitle}>{kn ? lesson.title_kn : lesson.title_en}</Text>
          <Text style={styles.contentText}>{lesson.content_en}</Text>

          {lesson.tips && lesson.tips.length > 0 && (
            <Card style={styles.tipsCard} variant="outlined">
              <View style={styles.tipsHeader}>
                <Ionicons name="bulb-outline" size={20} color={Colors.warning} />
                <Text style={styles.tipsTitle}>💡 Tips</Text>
              </View>
              {lesson.tips.map((tip, i) => (
                <View key={i} style={styles.tipRow}>
                  <Text style={styles.tipBullet}>✓</Text>
                  <Text style={styles.tipText}>{tip}</Text>
                </View>
              ))}
            </Card>
          )}

          <View style={styles.navRow}>
            {currentLessonIndex > 0 && (
              <Button
                title={t('common.back')}
                variant="outline"
                size="md"
                onPress={() => setCurrentLessonIndex(currentLessonIndex - 1)}
                style={{ flex: 1, marginRight: Spacing.sm }}
              />
            )}
            {currentLessonIndex < skillModule.lessons.length - 1 ? (
              <Button
                title={t('common.next')}
                size="md"
                onPress={() => setCurrentLessonIndex(currentLessonIndex + 1)}
                style={{ flex: 1 }}
              />
            ) : (
              <Button
                title={t('common.done')}
                size="md"
                onPress={() => setCurrentLessonIndex(null)}
                style={{ flex: 1 }}
              />
            )}
          </View>
        </ScrollView>
      </SafeAreaView>
    );
  }

  // Lesson list view
  const colorMap: Record<string, { color: string; bg: string }> = {
    digital_literacy: { color: '#3B82F6', bg: '#DBEAFE' },
    communication: { color: '#8B5CF6', bg: '#EDE9FE' },
    problem_solving: { color: '#F59E0B', bg: '#FEF3C7' },
    interview_skills: { color: '#10B981', bg: '#D1FAE5' },
  };
  const colors = colorMap[type || ''] || colorMap.digital_literacy;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
          <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
        </TouchableOpacity>
        <Text style={styles.headerTitle} numberOfLines={1}>{title}</Text>
        <View style={{ width: 40 }} />
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Module info */}
        <View style={[styles.moduleInfoCard, { backgroundColor: colors.bg }]}>
          <Text style={styles.moduleInfoTitle}>{title}</Text>
          <Text style={styles.moduleInfoDesc}>
            {skillModule.lessons.length} lessons to complete
          </Text>
        </View>

        {/* Lesson list */}
        {skillModule.lessons.map((lesson, index) => (
          <TouchableOpacity
            key={index}
            style={styles.lessonCard}
            onPress={() => setCurrentLessonIndex(index)}
            activeOpacity={0.7}
          >
            <View style={[styles.lessonNumber, { backgroundColor: colors.bg }]}>
              <Text style={[styles.lessonNumberText, { color: colors.color }]}>{index + 1}</Text>
            </View>
            <View style={styles.lessonInfo}>
              <Text style={styles.lessonName}>{kn ? lesson.title_kn : lesson.title_en}</Text>
              <Text style={styles.lessonMeta}>Tap to read</Text>
            </View>
            <Ionicons name="chevron-forward" size={20} color={Colors.textHint} />
          </TouchableOpacity>
        ))}
      </ScrollView>
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
    borderBottomWidth: 1,
    borderBottomColor: Colors.border,
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
  scrollContent: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxxl,
  },
  moduleInfoCard: {
    borderRadius: BorderRadius.lg,
    padding: Spacing.xl,
    marginBottom: Spacing.xl,
    alignItems: 'center',
  },
  moduleInfoTitle: {
    ...Typography.headingMD,
    textAlign: 'center',
    marginBottom: Spacing.xs,
  },
  moduleInfoDesc: {
    ...Typography.caption,
    color: Colors.textSecondary,
  },
  lessonCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.md,
    padding: Spacing.lg,
    marginBottom: Spacing.sm,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.03,
    shadowRadius: 4,
    elevation: 1,
  },
  lessonNumber: {
    width: 36,
    height: 36,
    borderRadius: 18,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: Spacing.md,
  },
  lessonNumberText: {
    ...Typography.bodyBold,
  },
  lessonInfo: {
    flex: 1,
  },
  lessonName: {
    ...Typography.bodyBold,
    marginBottom: 2,
  },
  lessonMeta: {
    ...Typography.caption,
    color: Colors.textHint,
  },
  lessonContent: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxxl,
  },
  lessonTitle: {
    ...Typography.headingLG,
    marginBottom: Spacing.xl,
  },
  contentText: {
    ...Typography.body,
    lineHeight: 26,
    marginBottom: Spacing.xxl,
  },
  tipsCard: {
    marginBottom: Spacing.xxl,
    backgroundColor: '#FFFBEB',
    borderColor: '#FDE68A',
  },
  tipsHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: Spacing.md,
    gap: Spacing.sm,
  },
  tipsTitle: {
    ...Typography.headingSM,
  },
  tipRow: {
    flexDirection: 'row',
    marginBottom: Spacing.xs,
    paddingLeft: Spacing.xs,
  },
  tipBullet: {
    color: Colors.success,
    marginRight: Spacing.sm,
    fontWeight: '700',
  },
  tipText: {
    ...Typography.body,
    flex: 1,
  },
  navRow: {
    flexDirection: 'row',
    marginTop: Spacing.lg,
  },
});
