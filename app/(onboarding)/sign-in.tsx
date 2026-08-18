import React, { useState } from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Colors } from '../../constants/colors';
import { Typography, Spacing } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { queryFirst } from '../../lib/db/database';
import { useAuth } from '../../hooks/useAuth';

export default function SignInScreen() {
  const router = useRouter();
  const { t } = useTranslation();
  const { signIn } = useAuth();

  const [phone, setPhone] = useState('');
  const [pin, setPin] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSignIn = async () => {
    if (!phone || !pin) {
      Alert.alert('Error', t('auth.phoneRequired'));
      return;
    }

    setIsLoading(true);
    try {
      const user = await queryFirst<{ id: number; name: string; role: string }>(
        'SELECT id, name, role FROM users WHERE phone = ? AND pin_hash = ?',
        [phone, pin]
      );

      if (user) {
        signIn(user);
        router.replace('/(tabs)/home');
      } else {
        Alert.alert('Error', 'Invalid phone number or PIN');
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
          <Text style={styles.title}>{t('auth.signInTitle')}</Text>
          <Text style={styles.subtitle}>{t('auth.signInSubtitle')}</Text>
        </View>

        <View style={styles.form}>
          <Input
            label="Phone"
            placeholder={t('auth.phonePlaceholder')}
            value={phone}
            onChangeText={setPhone}
            keyboardType="phone-pad"
          />
          <Input
            label={t('auth.pinLabel')}
            placeholder={t('auth.enterPin')}
            value={pin}
            onChangeText={setPin}
            keyboardType="number-pad"
            secureTextEntry
            maxLength={4}
          />

          <Button
            title={t('auth.signIn')}
            onPress={handleSignIn}
            loading={isLoading}
            style={styles.submitButton}
          />
          
          <Button
            title={t('auth.noAccount')}
            variant="ghost"
            onPress={() => router.replace('/(onboarding)/register')}
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
