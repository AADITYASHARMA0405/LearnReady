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
import { Card } from '../../components/ui/Card';
import { TabPills } from '../../components/ui/TabPills';
import { queryAll, queryFirst, execute } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';
import { isKannada } from '../../lib/i18n';

interface Lesson {
  id: number;
  module_id: number;
  title_en: string;
  title_kn: string;
  content_en: string;
  content_kn: string;
  key_points_en: string;
  key_points_kn: string;
  order_index: number;
}

interface ModuleInfo {
  id: number;
  title_en: string;
  title_kn: string;
  subject_id: number;
  question_count: number;
}

export default function LessonScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { t } = useTranslation();
  const { user } = useAuth();
  const [module, setModule] = useState<ModuleInfo | null>(null);
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [currentLessonIndex, setCurrentLessonIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [showKannada, setShowKannada] = useState(false);

  useEffect(() => {
    loadData();
  }, [id]);

  useEffect(() => {
    setShowKannada(isKannada());
  }, []);

  const loadData = async () => {
    try {
      const moduleData = await queryFirst<ModuleInfo>(`
        SELECT m.*, 
          (SELECT COUNT(*) FROM questions WHERE module_id = m.id) as question_count
        FROM modules m WHERE m.id = ?
      `, [Number(id)]);
      setModule(moduleData);

      const lessonData = await queryAll<Lesson>(
        'SELECT * FROM lessons WHERE module_id = ? ORDER BY order_index', [Number(id)]
      );
      setLessons(lessonData);

      // Track progress
      if (user && moduleData) {
        const existing = await queryFirst<{ id: number }>(
          'SELECT id FROM student_progress WHERE user_id = ? AND module_id = ?',
          [user.id, moduleData.id]
        );
        if (!existing) {
          await execute(
            'INSERT INTO student_progress (user_id, module_id, status, last_accessed) VALUES (?, ?, ?, datetime("now"))',
            [user.id, moduleData.id, 'in_progress']
          );
        } else {
          await execute(
            'UPDATE student_progress SET last_accessed = datetime("now"), status = CASE WHEN status = "locked" THEN "in_progress" ELSE status END WHERE user_id = ? AND module_id = ?',
            [user.id, moduleData.id]
          );
        }
      }
    } catch (error) {
      console.error('Error loading lesson:', error);
    } finally {
      setLoading(false);
    }
  };

  const currentLesson = lessons[currentLessonIndex];

  const getTitle = (item: any) => showKannada ? (item?.title_kn || item?.title_en || '') : (item?.title_en || '');
  const getContent = () => showKannada ? (currentLesson?.content_kn || currentLesson?.content_en || '') : (currentLesson?.content_en || '');
  const getKeyPoints = () => {
    const raw = showKannada ? (currentLesson?.key_points_kn || currentLesson?.key_points_en || '') : (currentLesson?.key_points_en || '');
    return raw.split('|').filter(Boolean);
  };

  if (loading || !module) {
    return (
      <SafeAreaView style={styles.container}>
        <ActivityIndicator size="large" color={Colors.primary} style={{ flex: 1 }} />
      </SafeAreaView>
    );
  }

  // If no lessons exist for this module, show info and quiz button
  if (lessons.length === 0) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity onPress={() => router.back()} style={styles.backBtn}>
            <Ionicons name="arrow-back" size={24} color={Colors.textDark} />
          </TouchableOpacity>
          <Text style={styles.headerTitle} numberOfLines={1}>{getTitle(module)}</Text>
          <View style={{ width: 40 }} />
        </View>
        <View style={styles.emptyContent}>
          <Ionicons name="document-text-outline" size={64} color={Colors.textHint} />
          <Text style={styles.emptyText}>Lessons coming soon</Text>
          {module.question_count > 0 && (
            <Button
              title={t('learn.takeQuiz')}
              onPress={() => router.push(`/quiz/${module.id}` as any)}
              style={{ marginTop: Spacing.xl }}
            />
          )}
        </View>
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
        <Text style={styles.headerTitle} numberOfLines={1}>{getTitle(module)}</Text>
        <TouchableOpacity
          onPress={() => setShowKannada(!showKannada)}
          style={styles.langBtn}
        >
          <Text style={styles.langBtnText}>{showKannada ? 'EN' : 'ಕ'}</Text>
        </TouchableOpacity>
      </View>

      {/* Lesson Tabs */}
      {lessons.length > 1 && (
        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.tabsRow}>
          {lessons.map((lesson, i) => (
            <TouchableOpacity
              key={lesson.id}
              style={[styles.tab, currentLessonIndex === i && styles.tabActive]}
              onPress={() => setCurrentLessonIndex(i)}
            >
              <Text style={[styles.tabText, currentLessonIndex === i && styles.tabTextActive]}>
                {i + 1}. {getTitle(lesson)}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      )}

      {/* Content */}
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Text style={styles.lessonTitle}>{getTitle(currentLesson)}</Text>
        
        <Text style={styles.contentText}>{getContent()}</Text>

        {/* Key Points */}
        {getKeyPoints().length > 0 && (
          <Card style={styles.keyPointsCard} variant="outlined">
            <View style={styles.keyPointsHeader}>
              <Ionicons name="bulb-outline" size={20} color={Colors.warning} />
              <Text style={styles.keyPointsTitle}>{t('learn.keyPoints')}</Text>
            </View>
            {getKeyPoints().map((point, i) => (
              <View key={i} style={styles.keyPoint}>
                <Text style={styles.keyPointBullet}>•</Text>
                <Text style={styles.keyPointText}>{point.trim()}</Text>
              </View>
            ))}
          </Card>
        )}

        {/* Navigation */}
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
          {currentLessonIndex < lessons.length - 1 ? (
            <Button
              title={t('common.next')}
              size="md"
              onPress={() => setCurrentLessonIndex(currentLessonIndex + 1)}
              style={{ flex: 1 }}
            />
          ) : module.question_count > 0 ? (
            <Button
              title={t('learn.takeQuiz')}
              size="md"
              onPress={() => router.push(`/quiz/${module.id}` as any)}
              style={{ flex: 1 }}
              icon={<Ionicons name="help-circle-outline" size={18} color={Colors.textWhite} />}
            />
          ) : (
            <Button
              title={t('common.done')}
              size="md"
              onPress={() => router.back()}
              style={{ flex: 1 }}
            />
          )}
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
  langBtn: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
  },
  langBtnText: {
    ...Typography.captionBold,
    color: Colors.primary,
  },
  tabsRow: {
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    borderBottomWidth: 1,
    borderBottomColor: Colors.border,
    maxHeight: 52,
  },
  tab: {
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    marginRight: Spacing.sm,
    borderRadius: BorderRadius.full,
    backgroundColor: Colors.bgInput,
  },
  tabActive: {
    backgroundColor: Colors.primary,
  },
  tabText: {
    ...Typography.caption,
    color: Colors.textSecondary,
  },
  tabTextActive: {
    color: Colors.textWhite,
    fontWeight: '600',
  },
  scrollContent: {
    padding: Spacing.lg,
    paddingBottom: Spacing.xxxl * 2,
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
  keyPointsCard: {
    marginBottom: Spacing.xxl,
    backgroundColor: '#FFFBEB',
    borderColor: '#FDE68A',
  },
  keyPointsHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: Spacing.md,
    gap: Spacing.sm,
  },
  keyPointsTitle: {
    ...Typography.headingSM,
    color: Colors.warning,
  },
  keyPoint: {
    flexDirection: 'row',
    marginBottom: Spacing.xs,
    paddingLeft: Spacing.xs,
  },
  keyPointBullet: {
    ...Typography.body,
    color: Colors.warning,
    marginRight: Spacing.sm,
    fontWeight: '700',
  },
  keyPointText: {
    ...Typography.body,
    flex: 1,
    color: Colors.textPrimary,
  },
  navRow: {
    flexDirection: 'row',
    marginTop: Spacing.lg,
  },
  emptyContent: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xxxl,
  },
  emptyText: {
    ...Typography.headingMD,
    color: Colors.textHint,
    marginTop: Spacing.lg,
  },
});
