import React, { useState } from 'react';
import { View, Text, StyleSheet, Dimensions, ScrollView, SafeAreaView } from 'react-native';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';
import { Button } from '../../components/ui/Button';
import { LanguageToggle } from '../../components/ui/LanguageToggle';
import { Ionicons } from '@expo/vector-icons';

const { width } = Dimensions.get('window');

export default function OnboardingScreen() {
  const router = useRouter();
  const { t } = useTranslation();
  const [currentIndex, setCurrentIndex] = useState(0);

  const slides = [
    {
      id: '1',
      title: t('onboarding.slide1Title'),
      desc: t('onboarding.slide1Desc'),
      icon: 'bulb-outline' as const,
      color: Colors.pastelBlue,
    },
    {
      id: '2',
      title: t('onboarding.slide2Title'),
      desc: t('onboarding.slide2Desc'),
      icon: 'briefcase-outline' as const,
      color: Colors.pastelPurple,
    },
    {
      id: '3',
      title: t('onboarding.slide3Title'),
      desc: t('onboarding.slide3Desc'),
      icon: 'language-outline' as const,
      color: Colors.pastelGreen,
    },
  ];

  const handleScroll = (event: any) => {
    const contentOffsetX = event.nativeEvent.contentOffset.x;
    const slideIndex = Math.round(contentOffsetX / width);
    setCurrentIndex(slideIndex);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <LanguageToggle />
      </View>

      <ScrollView
        horizontal
        pagingEnabled
        showsHorizontalScrollIndicator={false}
        onMomentumScrollEnd={handleScroll}
        style={styles.scrollView}
      >
        {slides.map((slide, index) => (
          <View key={slide.id} style={styles.slide}>
            <View style={[styles.imageContainer, { backgroundColor: slide.color }]}>
              <Ionicons name={slide.icon} size={100} color={Colors.textDark} />
            </View>
            <View style={styles.textContainer}>
              <Text style={styles.title}>{slide.title}</Text>
              <Text style={styles.description}>{slide.desc}</Text>
            </View>
          </View>
        ))}
      </ScrollView>

      <View style={styles.footer}>
        <View style={styles.pagination}>
          {slides.map((_, index) => (
            <View
              key={index}
              style={[
                styles.dot,
                currentIndex === index ? styles.activeDot : styles.inactiveDot,
              ]}
            />
          ))}
        </View>
        
        <View style={styles.buttonContainer}>
          <Button
            title={currentIndex === slides.length - 1 ? t('common.getStarted') : t('common.next')}
            onPress={() => {
              if (currentIndex === slides.length - 1) {
                router.push('/(onboarding)/register');
              } else {
                // In a real app we'd scroll to next using ref, but for simplicity here we let user swipe
                // or just jump to register if they tap next on last screen
              }
            }}
          />
          <Button
            title={t('auth.alreadyHaveAccount')}
            variant="ghost"
            onPress={() => router.push('/(onboarding)/sign-in')}
            style={styles.signInButton}
            textStyle={styles.signInText}
          />
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgMain,
  },
  header: {
    paddingHorizontal: Spacing.lg,
    paddingTop: Spacing.xl,
    alignItems: 'flex-end',
  },
  scrollView: {
    flex: 1,
  },
  slide: {
    width,
    alignItems: 'center',
    padding: Spacing.xl,
  },
  imageContainer: {
    width: width * 0.7,
    height: width * 0.7,
    borderRadius: BorderRadius.full,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: Spacing.xxl,
    marginBottom: Spacing.xxxl,
  },
  textContainer: {
    alignItems: 'center',
    paddingHorizontal: Spacing.lg,
  },
  title: {
    ...Typography.headingLG,
    textAlign: 'center',
    marginBottom: Spacing.md,
  },
  description: {
    ...Typography.body,
    textAlign: 'center',
    color: Colors.textSecondary,
  },
  footer: {
    padding: Spacing.xl,
    paddingBottom: Spacing.xxxl,
  },
  pagination: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: Spacing.xxl,
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginHorizontal: 4,
  },
  activeDot: {
    backgroundColor: Colors.primary,
    width: 24,
  },
  inactiveDot: {
    backgroundColor: Colors.border,
  },
  buttonContainer: {
    gap: Spacing.md,
  },
  signInButton: {
    marginTop: Spacing.sm,
  },
  signInText: {
    color: Colors.textSecondary,
  },
});
