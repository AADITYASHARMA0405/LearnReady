import React, { useEffect, useState } from 'react';
import {
  View, Text, StyleSheet, ScrollView, SafeAreaView,
  TouchableOpacity, Switch, Alert,
} from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { useAuth } from '../../hooks/useAuth';
import { isKannada } from '../../lib/i18n';
import i18n from '../../lib/i18n';
import { queryFirst } from '../../lib/db/database';

export default function ProfileScreen() {
  const router = useRouter();
  const { t, i18n } = useTranslation();
  const { user, logout } = useAuth();
  
  const [isKn, setIsKn] = useState(isKannada());
  const [stats, setStats] = useState({ completedModules: 0, avgScore: 0, quizzesTaken: 0 });

  useEffect(() => {
    loadStats();
  }, [user]);

  const loadStats = async () => {
    if (!user) return;
    try {
      const result = await queryFirst<{ count: number, avg: number, attempts: number }>(`
        SELECT 
          COUNT(CASE WHEN status = 'completed' THEN 1 END) as count,
          AVG(CASE WHEN status = 'completed' THEN best_score ELSE NULL END) as avg,
          SUM(attempts) as attempts
        FROM student_progress 
        WHERE user_id = ?
      `, [user.id]);
      
      if (result) {
        setStats({
          completedModules: result.count || 0,
          avgScore: Math.round(result.avg || 0),
          quizzesTaken: result.attempts || 0
        });
      }
    } catch (e) {
      console.error('Error loading stats', e);
    }
  };

  const toggleLanguage = async (value: boolean) => {
    const newLang = value ? 'kn' : 'en';
    await i18n.changeLanguage(newLang);
    setIsKn(value);
  };

  const handleLogout = () => {
    Alert.alert(
      t('profile.logout'),
      'Are you sure you want to log out?',
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: 'Log Out', 
          style: 'destructive',
          onPress: async () => {
            await logout();
            router.replace('/(onboarding)');
          }
        }
      ]
    );
  };

  if (!user) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.guestContainer}>
          <Ionicons name="person-circle-outline" size={80} color={Colors.textHint} />
          <Text style={styles.guestTitle}>Sign in to view your profile</Text>
          <TouchableOpacity 
            style={styles.signInBtn}
            onPress={() => router.replace('/(onboarding)/sign-in')}
          >
            <Text style={styles.signInText}>Sign In</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>{t('profile.title')}</Text>
        </View>

        {/* Profile Card */}
        <View style={styles.profileCard}>
          <View style={styles.avatar}>
            <Text style={styles.avatarText}>{(user.full_name || user.name || '?').charAt(0).toUpperCase()}</Text>
          </View>
          <View style={styles.profileInfo}>
            <Text style={styles.name}>{user.full_name || user.name}</Text>
            <Text style={styles.email}>{user.email || 'No email provided'}</Text>
            {user.diagnostic_completed === 1 && (
              <View style={styles.badge}>
                <Ionicons name="star" size={12} color="#D97706" />
                <Text style={styles.badgeText}>Assessed</Text>
              </View>
            )}
          </View>
        </View>

        {/* Stats Grid */}
        <Text style={styles.sectionTitle}>{t('profile.progress')}</Text>
        <View style={styles.statsGrid}>
          <View style={styles.statBox}>
            <Ionicons name="checkmark-done-circle-outline" size={24} color={Colors.success} />
            <Text style={styles.statValue}>{stats.completedModules}</Text>
            <Text style={styles.statLabel}>Modules</Text>
          </View>
          <View style={styles.statBox}>
            <Ionicons name="trophy-outline" size={24} color={Colors.warning} />
            <Text style={styles.statValue}>{stats.avgScore}%</Text>
            <Text style={styles.statLabel}>Avg Score</Text>
          </View>
          <View style={styles.statBox}>
            <Ionicons name="create-outline" size={24} color={Colors.primary} />
            <Text style={styles.statValue}>{stats.quizzesTaken}</Text>
            <Text style={styles.statLabel}>Quizzes</Text>
          </View>
        </View>

        {/* Settings */}
        <Text style={styles.sectionTitle}>{t('profile.settings')}</Text>
        
        <View style={styles.settingsGroup}>
          <View style={styles.settingItem}>
            <View style={styles.settingLeft}>
              <Ionicons name="language-outline" size={24} color={Colors.textSecondary} />
              <Text style={styles.settingLabel}>{t('profile.language')} (ಕನ್ನಡ)</Text>
            </View>
            <Switch
              value={isKn}
              onValueChange={toggleLanguage}
              trackColor={{ false: Colors.border, true: Colors.primaryLight }}
              thumbColor={isKn ? Colors.primary : Colors.textHint}
            />
          </View>

          <TouchableOpacity style={styles.settingItem}>
            <View style={styles.settingLeft}>
              <Ionicons name="notifications-outline" size={24} color={Colors.textSecondary} />
              <Text style={styles.settingLabel}>Notifications</Text>
            </View>
            <Ionicons name="chevron-forward" size={20} color={Colors.textHint} />
          </TouchableOpacity>

          <TouchableOpacity style={styles.settingItem}>
            <View style={styles.settingLeft}>
              <Ionicons name="shield-checkmark-outline" size={24} color={Colors.textSecondary} />
              <Text style={styles.settingLabel}>Privacy & Security</Text>
            </View>
            <Ionicons name="chevron-forward" size={20} color={Colors.textHint} />
          </TouchableOpacity>
        </View>

        <TouchableOpacity style={styles.logoutBtn} onPress={handleLogout}>
          <Ionicons name="log-out-outline" size={24} color={Colors.error} />
          <Text style={styles.logoutText}>{t('profile.logout')}</Text>
        </TouchableOpacity>
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
    marginBottom: Spacing.xl,
    paddingTop: Spacing.md,
  },
  title: {
    ...Typography.headingLG,
  },
  profileCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.bgCard,
    padding: Spacing.xl,
    borderRadius: BorderRadius.lg,
    marginBottom: Spacing.xxl,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  avatar: {
    width: 64,
    height: 64,
    borderRadius: 32,
    backgroundColor: Colors.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: Spacing.lg,
  },
  avatarText: {
    ...Typography.headingLG,
    color: Colors.primary,
  },
  profileInfo: {
    flex: 1,
  },
  name: {
    ...Typography.headingMD,
    marginBottom: 4,
  },
  email: {
    ...Typography.caption,
    color: Colors.textSecondary,
    marginBottom: 8,
  },
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FEF3C7',
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: BorderRadius.full,
    gap: 4,
  },
  badgeText: {
    ...Typography.captionBold,
    color: '#D97706',
    fontSize: 10,
  },
  sectionTitle: {
    ...Typography.headingSM,
    marginBottom: Spacing.md,
  },
  statsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: Spacing.xxl,
    gap: Spacing.sm,
  },
  statBox: {
    flex: 1,
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    padding: Spacing.lg,
    alignItems: 'center',
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.03,
    shadowRadius: 4,
    elevation: 1,
  },
  statValue: {
    ...Typography.headingMD,
    marginTop: Spacing.sm,
    marginBottom: 2,
  },
  statLabel: {
    ...Typography.caption,
    color: Colors.textSecondary,
  },
  settingsGroup: {
    backgroundColor: Colors.bgCard,
    borderRadius: BorderRadius.lg,
    marginBottom: Spacing.xxl,
    overflow: 'hidden',
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.03,
    shadowRadius: 4,
    elevation: 1,
  },
  settingItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: Spacing.lg,
    borderBottomWidth: 1,
    borderBottomColor: Colors.bgMain,
  },
  settingLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.md,
  },
  settingLabel: {
    ...Typography.body,
  },
  logoutBtn: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: Spacing.sm,
    padding: Spacing.lg,
    borderRadius: BorderRadius.md,
    backgroundColor: '#FEF2F2',
  },
  logoutText: {
    ...Typography.bodyBold,
    color: Colors.error,
  },
  guestContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xxxl,
  },
  guestTitle: {
    ...Typography.headingSM,
    color: Colors.textSecondary,
    marginTop: Spacing.lg,
    marginBottom: Spacing.xxl,
  },
  signInBtn: {
    backgroundColor: Colors.primary,
    paddingHorizontal: Spacing.xxl,
    paddingVertical: Spacing.md,
    borderRadius: BorderRadius.full,
  },
  signInText: {
    ...Typography.bodyBold,
    color: Colors.textWhite,
  }
});
