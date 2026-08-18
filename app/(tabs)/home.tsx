import React from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, TouchableOpacity } from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Card } from '../../components/ui/Card';
import { CircularProgress } from '../../components/ui/CircularProgress';
import { SubjectIcon } from '../../components/ui/SubjectIcon';
import { LanguageToggle } from '../../components/ui/LanguageToggle';
import { useAuth } from '../../hooks/useAuth';

export default function HomeScreen() {
  const router = useRouter();
  const { t } = useTranslation();
  const { user } = useAuth();

  // Mock data for demo
  const jobReadinessScore = 45;
  const subjects = [
    { id: 'math', name: 'Mathematics', icon: 'calculator-outline' as const },
    { id: 'cs', name: 'Computer Science', icon: 'laptop-outline' as const },
    { id: 'physics', name: 'Physics', icon: 'planet-outline' as const },
    { id: 'mech', name: 'Mechanical', icon: 'cog-outline' as const },
  ];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>
              {t('home.greeting', { name: user?.name?.split(' ')[0] || 'Student' })}
            </Text>
          </View>
          <LanguageToggle />
        </View>

        {/* Job Readiness Card */}
        <Card style={styles.readinessCard} variant="elevated">
          <View style={styles.readinessContent}>
            <View style={styles.readinessText}>
              <Text style={styles.readinessTitle}>{t('home.jobReadiness')}</Text>
              <Text style={styles.readinessSubtitle}>{t('home.jobReadinessSubtitle')}</Text>
              <TouchableOpacity
                style={styles.continueButton}
                onPress={() => router.push('/(tabs)/skills')}
              >
                <Text style={styles.continueButtonText}>{t('home.continueLearning')}</Text>
              </TouchableOpacity>
            </View>
            <CircularProgress score={jobReadinessScore} size={90} />
          </View>
        </Card>

        {/* My Subjects */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>{t('home.mySubjects')}</Text>
            <TouchableOpacity onPress={() => router.push('/(tabs)/learn')}>
              <Text style={styles.seeAll}>{t('common.seeAll')}</Text>
            </TouchableOpacity>
          </View>

          <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.subjectsList}>
            {subjects.map((subject) => (
              <TouchableOpacity
                key={subject.id}
                onPress={() => router.push(`/(tabs)/learn`)}
                activeOpacity={0.7}
              >
                <SubjectIcon
                  id={subject.id}
                  name={subject.name}
                  iconName={subject.icon}
                  style={styles.subjectItem}
                />
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>

        {/* Today's Plan */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>{t('home.todaysPlan')}</Text>
          <Card style={styles.planCard}>
            <View style={styles.planIcon}>
              <SubjectIcon id="cs" name="" iconName="laptop-outline" size="sm" />
            </View>
            <View style={styles.planTextContainer}>
              <Text style={styles.planTitle}>Data Structures</Text>
              <Text style={styles.planSubtitle}>Chapter 3: Trees • 15 mins left</Text>
            </View>
            <TouchableOpacity style={styles.playButton}>
              <Text style={styles.playIcon}>▶</Text>
            </TouchableOpacity>
          </Card>
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
  greeting: {
    ...Typography.headingLG,
  },
  readinessCard: {
    marginBottom: Spacing.xxl,
  },
  readinessContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  readinessText: {
    flex: 1,
    paddingRight: Spacing.md,
  },
  readinessTitle: {
    ...Typography.headingMD,
    marginBottom: Spacing.xs,
  },
  readinessSubtitle: {
    ...Typography.body,
    color: Colors.textSecondary,
    marginBottom: Spacing.lg,
  },
  continueButton: {
    backgroundColor: Colors.primaryLight,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: BorderRadius.full,
    alignSelf: 'flex-start',
  },
  continueButtonText: {
    ...Typography.captionBold,
    color: Colors.primary,
  },
  section: {
    marginBottom: Spacing.xxl,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  sectionTitle: {
    ...Typography.headingMD,
  },
  seeAll: {
    ...Typography.bodyBold,
    color: Colors.primary,
  },
  subjectsList: {
    marginHorizontal: -Spacing.lg,
    paddingHorizontal: Spacing.lg,
  },
  subjectItem: {
    marginRight: Spacing.md,
  },
  planCard: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: Spacing.md,
    marginTop: Spacing.sm,
  },
  planIcon: {
    marginRight: Spacing.md,
  },
  planTextContainer: {
    flex: 1,
  },
  planTitle: {
    ...Typography.bodyBold,
    marginBottom: 4,
  },
  planSubtitle: {
    ...Typography.caption,
    color: Colors.textSecondary,
  },
  playButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
  },
  playIcon: {
    color: Colors.primary,
    fontSize: 18,
    marginLeft: 4,
  },
});
