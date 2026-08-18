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
import { queryAll, queryFirst } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';
import { isKannada } from '../../lib/i18n';

interface SubjectInfo {
  id: number;
  name_en: string;
  name_kn: string;
  color: string;
  icon_name: string;
}

interface Module {
  id: number;
  title_en: string;
  title_kn: string;
  description_en: string;
  description_kn: string;
  order_index: number;
  difficulty: string;
  status: string | null;
  score: number | null;
  lesson_count: number;
  question_count: number;
}

export default function SubjectDetailScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { t } = useTranslation();
  const { user } = useAuth();
  const [subject, setSubject] = useState<SubjectInfo | null>(null);
  const [modules, setModules] = useState<Module[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, [id]);

  const loadData = async () => {
    try {
      const subjectData = await queryFirst<SubjectInfo>(
        'SELECT * FROM subjects WHERE id = ?', [Number(id)]
      );
      setSubject(subjectData);

      const moduleData = await queryAll<Module>(`
        SELECT m.*,
          sp.status, sp.best_score as score,
          (SELECT COUNT(*) FROM lessons WHERE module_id = m.id) as lesson_count,
          (SELECT COUNT(*) FROM questions WHERE module_id = m.id) as question_count
        FROM modules m
        LEFT JOIN student_progress sp ON sp.module_id = m.id AND sp.user_id = ?
        WHERE m.subject_id = ?
        ORDER BY m.order_index
      `, [user?.id || 0, Number(id)]);
      setModules(moduleData);
    } catch (error) {
      console.error('Error loading subject:', error);
    } finally {
      setLoading(false);
    }
  };

  const getName = (item: { name_en?: string; name_kn?: string; title_en?: string; title_kn?: string }) => {
    if (isKannada()) return item.name_kn || item.title_kn || item.name_en || item.title_en || '';
    return item.name_en || item.title_en || '';
  };

  const getDesc = (item: { description_en?: string; description_kn?: string }) => {
    return isKannada() ? (item.description_kn || item.description_en || '') : (item.description_en || '');
  };

  const getStatusIcon = (status: string | null, index: number) => {
    if (status === 'completed') return { name: 'checkmark-circle' as const, color: Colors.success };
    if (status === 'in_progress') return { name: 'time-outline' as const, color: Colors.primary };
    if (index === 0 || status === 'available') return { name: 'radio-button-off-outline' as const, color: Colors.textHint };
    return { name: 'lock-closed-outline' as const, color: Colors.textHint };
  };

  const getDifficultyBadge = (difficulty: string) => {
    const map: Record<string, { label: string; color: string; bg: string }> = {
      easy: { label: 'Easy', color: '#16A34A', bg: '#F0FDF4' },
      medium: { label: 'Medium', color: '#D97706', bg: '#FFFBEB' },
      hard: { label: 'Hard', color: '#DC2626', bg: '#FEF2F2' },
    };
    return map[difficulty] || map.easy;
  };

  if (loading || !subject) {
    return (
      <SafeAreaView style={styles.container}>
        <ActivityIndicator size="large" color={Colors.primary} style={{ flex: 1 }} />
      </SafeAreaView>
    );
  }

  const iconMap: Record<string, keyof typeof Ionicons.glyphMap> = {
    'calculator-outline': 'calculator-outline',
    'laptop-outline': 'laptop-outline',
    'planet-outline': 'planet-outline',
    'cog-outline': 'cog-outline',
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={[styles.headerBanner, { backgroundColor: subject.color }]}>
        <TouchableOpacity onPress={() => router.back()} style={styles.backButton}>
          <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
        </TouchableOpacity>
        <View style={styles.headerContent}>
          <Ionicons
            name={iconMap[subject.icon_name] || 'book-outline'}
            size={48}
            color={Colors.textDark}
          />
          <Text style={styles.headerTitle}>{getName(subject)}</Text>
          <Text style={styles.headerSubtitle}>
            {modules.length} {t('learn.chapters')} • {modules.reduce((s, m) => s + m.question_count, 0)} {t('learn.questions')}
          </Text>
        </View>
      </View>

      {/* Chapter List */}
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Text style={styles.sectionTitle}>{t('learn.selectChapter')}</Text>
        {modules.map((module, index) => {
          const statusIcon = getStatusIcon(module.status, index);
          const diff = getDifficultyBadge(module.difficulty);

          return (
            <TouchableOpacity
              key={module.id}
              style={styles.chapterCard}
              onPress={() => router.push(`/lesson/${module.id}` as any)}
              activeOpacity={0.7}
            >
              <View style={styles.chapterLeft}>
                <View style={styles.chapterNumber}>
                  <Text style={styles.chapterNumberText}>{index + 1}</Text>
                </View>
                {index < modules.length - 1 && <View style={styles.chapterLine} />}
              </View>
              <View style={styles.chapterContent}>
                <View style={styles.chapterHeader}>
                  <Text style={styles.chapterTitle} numberOfLines={1}>
                    {getName(module)}
                  </Text>
                  <Ionicons name={statusIcon.name} size={24} color={statusIcon.color} />
                </View>
                <Text style={styles.chapterDesc} numberOfLines={2}>
                  {getDesc(module)}
                </Text>
                <View style={styles.chapterMeta}>
                  <View style={[styles.diffBadge, { backgroundColor: diff.bg }]}>
                    <Text style={[styles.diffText, { color: diff.color }]}>{diff.label}</Text>
                  </View>
                  {module.lesson_count > 0 && (
                    <Text style={styles.metaText}>
                      <Ionicons name="document-text-outline" size={12} color={Colors.textHint} /> {module.lesson_count} {t('learn.lessons')}
                    </Text>
                  )}
                  {module.question_count > 0 && (
                    <Text style={styles.metaText}>
                      <Ionicons name="help-circle-outline" size={12} color={Colors.textHint} /> {module.question_count} Q
                    </Text>
                  )}
                </View>
                {module.score !== null && module.score > 0 && (
                  <Text style={styles.scoreText}>
                    Best score: {Math.round(module.score)}%
                  </Text>
                )}
              </View>
            </TouchableOpacity>
          );
        })}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgMain,
  },
  headerBanner: {
    paddingTop: Spacing.xl,
    paddingBottom: Spacing.xxl,
    paddingHorizontal: Spacing.lg,
    borderBottomLeftRadius: BorderRadius.xl,
    borderBottomRightRadius: BorderRadius.xl,
  },
  backButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: 'rgba(255,255,255,0.5)',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  headerContent: {
    alignItems: 'center',
  },
  headerTitle: {
    ...Typography.headingLG,
    marginTop: Spacing.md,
    textAlign: 'center',
  },
  headerSubtitle: {
    ...Typography.body,
    color: Colors.textSecondary,
    marginTop: Spacing.xs,
  },
  scrollContent: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxxl,
  },
  sectionTitle: {
    ...Typography.headingMD,
    marginBottom: Spacing.lg,
  },
  chapterCard: {
    flexDirection: 'row',
    marginBottom: Spacing.sm,
  },
  chapterLeft: {
    alignItems: 'center',
    width: 40,
    marginRight: Spacing.md,
  },
  chapterNumber: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1,
  },
  chapterNumberText: {
    ...Typography.captionBold,
    color: Colors.primary,
  },
  chapterLine: {
    width: 2,
    flex: 1,
    backgroundColor: Colors.border,
    marginTop: -2,
  },
  chapterContent: {
    flex: 1,
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
  chapterHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 4,
  },
  chapterTitle: {
    ...Typography.headingSM,
    flex: 1,
    marginRight: Spacing.sm,
  },
  chapterDesc: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginBottom: Spacing.sm,
  },
  chapterMeta: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
  },
  diffBadge: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: BorderRadius.sm,
  },
  diffText: {
    fontSize: 11,
    fontWeight: '600',
  },
  metaText: {
    ...Typography.caption,
    color: Colors.textHint,
    fontSize: 11,
  },
  scoreText: {
    ...Typography.captionBold,
    color: Colors.success,
    marginTop: Spacing.xs,
  },
});
