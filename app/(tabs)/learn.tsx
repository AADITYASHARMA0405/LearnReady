import React, { useEffect, useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity, ActivityIndicator,
} from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors, SubjectColors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { ProgressBar } from '../../components/ui/ProgressBar';
import { LanguageToggle } from '../../components/ui/LanguageToggle';
import { queryAll, queryFirst } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';
import { isKannada } from '../../lib/i18n';

interface Subject {
  id: number;
  name_en: string;
  name_kn: string;
  icon_name: string;
  color: string;
  module_count: number;
  completed_count: number;
}

export default function LearnScreen() {
  const router = useRouter();
  const { t, i18n } = useTranslation();
  const { user } = useAuth();
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSubjects();
  }, []);

  const loadSubjects = async () => {
    try {
      const data = await queryAll<Subject>(`
        SELECT s.*, 
          (SELECT COUNT(*) FROM modules WHERE subject_id = s.id) as module_count,
          (SELECT COUNT(*) FROM student_progress sp 
           JOIN modules m ON sp.module_id = m.id 
           WHERE m.subject_id = s.id AND sp.user_id = ? AND sp.status = 'completed') as completed_count
        FROM subjects s
        ORDER BY s.id
      `, [user?.id || 0]);
      setSubjects(data);
    } catch (error) {
      console.error('Error loading subjects:', error);
    } finally {
      setLoading(false);
    }
  };

  const getSubjectName = (subject: Subject) => {
    return isKannada() ? (subject.name_kn || subject.name_en) : subject.name_en;
  };

  const getProgress = (subject: Subject) => {
    if (subject.module_count === 0) return 0;
    return Math.round((subject.completed_count / subject.module_count) * 100);
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <ActivityIndicator size="large" color={Colors.primary} style={{ flex: 1 }} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>{t('learn.allSubjects')}</Text>
          <LanguageToggle />
        </View>

        {/* Subject Grid */}
        <View style={styles.grid}>
          {subjects.map((subject) => {
            const progress = getProgress(subject);
            const iconMap: Record<string, keyof typeof Ionicons.glyphMap> = {
              'calculator-outline': 'calculator-outline',
              'laptop-outline': 'laptop-outline',
              'planet-outline': 'planet-outline',
              'cog-outline': 'cog-outline',
            };
            const iconName = iconMap[subject.icon_name] || 'book-outline';

            return (
              <TouchableOpacity
                key={subject.id}
                style={[styles.subjectCard, { borderLeftColor: subject.color }]}
                onPress={() => router.push(`/subject/${subject.id}` as any)}
                activeOpacity={0.7}
              >
                <View style={[styles.iconCircle, { backgroundColor: subject.color }]}>
                  <Ionicons name={iconName} size={28} color={Colors.textDark} />
                </View>
                <Text style={styles.subjectName} numberOfLines={1}>
                  {getSubjectName(subject)}
                </Text>
                <Text style={styles.chapterCount}>
                  {subject.module_count} {t('learn.chapters')}
                </Text>
                <ProgressBar
                  progress={progress}
                  height={6}
                  color={subject.color === '#BFDBFE' ? Colors.primary : subject.color}
                  showLabel
                  style={styles.progressBar}
                />
              </TouchableOpacity>
            );
          })}
        </View>

        {/* Quick Stats */}
        <View style={styles.statsRow}>
          <View style={styles.statCard}>
            <Ionicons name="book-outline" size={24} color={Colors.primary} />
            <Text style={styles.statNumber}>{subjects.length}</Text>
            <Text style={styles.statLabel}>{t('learn.allSubjects')}</Text>
          </View>
          <View style={styles.statCard}>
            <Ionicons name="layers-outline" size={24} color={Colors.success} />
            <Text style={styles.statNumber}>
              {subjects.reduce((sum, s) => sum + s.module_count, 0)}
            </Text>
            <Text style={styles.statLabel}>{t('learn.chapters')}</Text>
          </View>
          <View style={styles.statCard}>
            <Ionicons name="checkmark-circle-outline" size={24} color={Colors.warning} />
            <Text style={styles.statNumber}>
              {subjects.reduce((sum, s) => sum + s.completed_count, 0)}
            </Text>
            <Text style={styles.statLabel}>{t('learn.completed')}</Text>
          </View>
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
    alignItems: 'center',
    marginBottom: Spacing.xl,
    paddingTop: Spacing.md,
  },
  title: {
    ...Typography.headingLG,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginBottom: Spacing.xxl,
  },
  subjectCard: {
    width: '48%',
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    marginBottom: Spacing.md,
    borderLeftWidth: 4,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 2,
  },
  iconCircle: {
    width: 52,
    height: 52,
    borderRadius: 26,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  subjectName: {
    ...Typography.headingSM,
    marginBottom: 4,
  },
  chapterCount: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginBottom: Spacing.md,
  },
  progressBar: {
    marginTop: 'auto',
  },
  statsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: Spacing.sm,
  },
  statCard: {
    flex: 1,
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.md,
    padding: Spacing.md,
    alignItems: 'center',
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.03,
    shadowRadius: 4,
    elevation: 1,
  },
  statNumber: {
    ...Typography.headingMD,
    marginTop: Spacing.xs,
  },
  statLabel: {
    ...Typography.caption,
    textAlign: 'center',
    marginTop: 2,
  },
});
