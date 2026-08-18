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
import { ProgressBar } from '../../components/ui/ProgressBar';
import { LanguageToggle } from '../../components/ui/LanguageToggle';

interface SkillModule {
  type: string;
  title: string;
  description: string;
  icon: keyof typeof Ionicons.glyphMap;
  color: string;
  bgColor: string;
  progress: number;
  lessonCount: number;
}

export default function SkillsScreen() {
  const router = useRouter();
  const { t } = useTranslation();

  const skills: SkillModule[] = [
    {
      type: 'digital_literacy',
      title: t('skills.digitalLiteracy'),
      description: t('skills.digitalLiteracyDesc'),
      icon: 'desktop-outline',
      color: '#3B82F6',
      bgColor: '#DBEAFE',
      progress: 0,
      lessonCount: 3,
    },
    {
      type: 'communication',
      title: t('skills.communicationTitle'),
      description: t('skills.communicationDesc'),
      icon: 'chatbubbles-outline',
      color: '#8B5CF6',
      bgColor: '#EDE9FE',
      progress: 0,
      lessonCount: 2,
    },
    {
      type: 'problem_solving',
      title: t('skills.problemSolvingTitle'),
      description: t('skills.problemSolvingDesc'),
      icon: 'bulb-outline',
      color: '#F59E0B',
      bgColor: '#FEF3C7',
      progress: 0,
      lessonCount: 2,
    },
    {
      type: 'interview_skills',
      title: t('skills.interviewSkillsTitle'),
      description: t('skills.interviewSkillsDesc'),
      icon: 'briefcase-outline',
      color: '#10B981',
      bgColor: '#D1FAE5',
      progress: 0,
      lessonCount: 2,
    },
  ];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.title}>{t('skills.title')}</Text>
            <Text style={styles.subtitle}>Build skills for your career</Text>
          </View>
          <LanguageToggle />
        </View>

        {/* Overall Progress Card */}
        <View style={styles.overallCard}>
          <View style={styles.overallLeft}>
            <Text style={styles.overallTitle}>{t('home.skillReadiness')}</Text>
            <ProgressBar progress={0} height={8} showLabel style={{ marginTop: Spacing.sm }} />
          </View>
          <View style={styles.overallIcon}>
            <Ionicons name="ribbon-outline" size={36} color={Colors.primary} />
          </View>
        </View>

        {/* Skill Cards */}
        {skills.map((skill) => (
          <TouchableOpacity
            key={skill.type}
            style={styles.skillCard}
            onPress={() => router.push(`/skill/${skill.type}` as any)}
            activeOpacity={0.7}
          >
            <View style={styles.skillCardTop}>
              <View style={[styles.skillIcon, { backgroundColor: skill.bgColor }]}>
                <Ionicons name={skill.icon} size={28} color={skill.color} />
              </View>
              <Ionicons name="chevron-forward" size={20} color={Colors.textHint} />
            </View>
            <Text style={styles.skillTitle}>{skill.title}</Text>
            <Text style={styles.skillDesc} numberOfLines={2}>{skill.description}</Text>
            <View style={styles.skillBottom}>
              <Text style={styles.lessonCount}>
                {t('skills.lessonsCount', { count: skill.lessonCount })}
              </Text>
              <ProgressBar
                progress={skill.progress}
                height={6}
                color={skill.color}
                trackColor={skill.bgColor}
                style={{ flex: 1, marginLeft: Spacing.md }}
              />
            </View>
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
  overallCard: {
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: Spacing.xl,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  overallLeft: {
    flex: 1,
    marginRight: Spacing.lg,
  },
  overallTitle: {
    ...Typography.headingSM,
  },
  overallIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
  },
  skillCard: {
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    marginBottom: Spacing.md,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 1,
  },
  skillCardTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  skillIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
  },
  skillTitle: {
    ...Typography.headingSM,
    marginBottom: 4,
  },
  skillDesc: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginBottom: Spacing.md,
  },
  skillBottom: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  lessonCount: {
    ...Typography.captionBold,
    color: Colors.textHint,
  },
});
