import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View, ViewStyle } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../../constants/colors';
import { Typography, BorderRadius, Spacing } from '../../constants/typography';

interface QuizOptionProps {
  label: string;
  index: number;
  selected: boolean;
  state?: 'default' | 'correct' | 'wrong';
  onPress: () => void;
  disabled?: boolean;
  style?: ViewStyle;
}

const OPTION_LETTERS = ['A', 'B', 'C', 'D', 'E', 'F'];

export const QuizOption: React.FC<QuizOptionProps> = ({
  label,
  index,
  selected,
  state = 'default',
  onPress,
  disabled = false,
  style,
}) => {
  const isCorrect = state === 'correct';
  const isWrong = state === 'wrong';
  const hasResult = isCorrect || isWrong;

  const containerStyle = [
    styles.container,
    selected && !hasResult && styles.selected,
    isCorrect && styles.correct,
    isWrong && styles.wrong,
    style,
  ];

  const letterStyle = [
    styles.letter,
    selected && !hasResult && styles.letterSelected,
    isCorrect && styles.letterCorrect,
    isWrong && styles.letterWrong,
  ];

  const letterBgStyle = [
    styles.letterBg,
    selected && !hasResult && styles.letterBgSelected,
    isCorrect && styles.letterBgCorrect,
    isWrong && styles.letterBgWrong,
  ];

  return (
    <TouchableOpacity
      style={containerStyle}
      onPress={onPress}
      disabled={disabled}
      activeOpacity={0.7}
    >
      <View style={letterBgStyle}>
        <Text style={letterStyle}>{OPTION_LETTERS[index]}</Text>
      </View>
      <Text style={[styles.label, hasResult && isCorrect && styles.labelCorrect, hasResult && isWrong && styles.labelWrong]} numberOfLines={3}>
        {label}
      </Text>
      {isCorrect && (
        <Ionicons name="checkmark-circle" size={22} color={Colors.success} style={styles.icon} />
      )}
      {isWrong && (
        <Ionicons name="close-circle" size={22} color={Colors.error} style={styles.icon} />
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.bgCard,
    borderWidth: 1.5,
    borderColor: Colors.border,
    borderRadius: BorderRadius.md,
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.lg,
    marginBottom: Spacing.sm,
  },
  selected: {
    borderColor: Colors.primary,
    backgroundColor: '#EEF2FF',
  },
  correct: {
    borderColor: Colors.success,
    backgroundColor: '#F0FDF4',
  },
  wrong: {
    borderColor: Colors.error,
    backgroundColor: '#FEF2F2',
  },
  letterBg: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: Colors.bgInput,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: Spacing.md,
  },
  letterBgSelected: {
    backgroundColor: Colors.primaryLight,
  },
  letterBgCorrect: {
    backgroundColor: '#DCFCE7',
  },
  letterBgWrong: {
    backgroundColor: '#FEE2E2',
  },
  letter: {
    ...Typography.bodyBold,
    color: Colors.textSecondary,
  },
  letterSelected: {
    color: Colors.primary,
  },
  letterCorrect: {
    color: Colors.success,
  },
  letterWrong: {
    color: Colors.error,
  },
  label: {
    ...Typography.body,
    flex: 1,
    color: Colors.textDark,
  },
  labelCorrect: {
    color: Colors.success,
    fontWeight: '600',
  },
  labelWrong: {
    color: Colors.error,
  },
  icon: {
    marginLeft: Spacing.sm,
  },
});
