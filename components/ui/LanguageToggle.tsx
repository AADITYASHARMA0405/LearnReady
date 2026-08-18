import React from 'react';
import { TouchableOpacity, Text, StyleSheet, ViewStyle } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useTranslation } from 'react-i18next';
import { toggleLanguage } from '../../lib/i18n';
import { Colors } from '../../constants/colors';
import { Typography, BorderRadius } from '../../constants/typography';

interface LanguageToggleProps {
  style?: ViewStyle;
}

export const LanguageToggle: React.FC<LanguageToggleProps> = ({ style }) => {
  const { i18n } = useTranslation();
  const currentLang = i18n.language;

  const handleToggle = () => {
    toggleLanguage();
  };

  return (
    <TouchableOpacity
      style={[styles.container, style]}
      onPress={handleToggle}
      activeOpacity={0.7}
    >
      <Ionicons name="globe-outline" size={18} color={Colors.primary} />
      <Text style={styles.text}>
        {currentLang === 'en' ? 'ಕನ್ನಡ' : 'English'}
      </Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.bgCard,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: BorderRadius.full,
    borderWidth: 1,
    borderColor: Colors.primaryLight,
    gap: 6,
  },
  text: {
    ...Typography.captionBold,
    color: Colors.primary,
  },
});
