import React, { useState } from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Colors } from '../../constants/colors';
import { Typography, Spacing } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { execute, queryFirst } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';

export default function RegisterScreen() {
  const router = useRouter();
  const { t } = useTranslation();
  const { signIn } = useAuth();

  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [branch, setBranch] = useState('');
  const [pin, setPin] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleRegister = async () => {
    if (!name || !phone || !pin) {
      Alert.alert('Error', t('auth.nameRequired'));
      return;
    }

    setIsLoading(true);
    try {
      // In a real app we would hash the PIN. For this demo, we store it plain.
      await execute(
        'INSERT INTO users (name, phone, pin_hash, stream, role) VALUES (?, ?, ?, ?, ?)',
        [name, phone, pin, branch, 'student']
      );

      const newUser = await queryFirst<{ id: number; name: string; role: string }>(
        'SELECT id, name, role FROM users WHERE phone = ?',
        [phone]
      );

      if (newUser) {
        signIn(newUser);
        router.replace('/(tabs)/home'); // Skip diagnostic test for now, go straight to home
      }
    } catch (error) {
      console.error(error);
      Alert.alert('Error', t('common.error'));
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <Text style={styles.title}>{t('auth.signUpTitle')}</Text>
          <Text style={styles.subtitle}>{t('auth.signUpSubtitle')}</Text>
        </View>

        <View style={styles.form}>
          <Input
            label={t('auth.nameLabel')}
            placeholder={t('auth.namePlaceholder')}
            value={name}
            onChangeText={setName}
          />
          <Input
            label="Phone"
            placeholder={t('auth.phonePlaceholder')}
            value={phone}
            onChangeText={setPhone}
            keyboardType="phone-pad"
          />
          <Input
            label="Branch"
            placeholder={t('auth.branchPlaceholder')}
            value={branch}
            onChangeText={setBranch}
          />
          <Input
            label={t('auth.pinLabel')}
            placeholder={t('auth.pinPlaceholder')}
            value={pin}
            onChangeText={setPin}
            keyboardType="number-pad"
            secureTextEntry
            maxLength={4}
          />

          <Button
            title={t('auth.createAccount')}
            onPress={handleRegister}
            loading={isLoading}
            style={styles.submitButton}
          />
          
          <Button
            title={t('auth.alreadyHaveAccount')}
            variant="ghost"
            onPress={() => router.replace('/(onboarding)/sign-in')}
            textStyle={styles.linkText}
          />
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
    flexGrow: 1,
    padding: Spacing.xl,
    paddingTop: Spacing.xxxl,
  },
  header: {
    marginBottom: Spacing.xxxl,
  },
  title: {
    ...Typography.headingXL,
    marginBottom: Spacing.xs,
  },
  subtitle: {
    ...Typography.body,
    color: Colors.textSecondary,
  },
  form: {
    gap: Spacing.md,
  },
  submitButton: {
    marginTop: Spacing.xl,
  },
  linkText: {
    color: Colors.textSecondary,
  },
});
