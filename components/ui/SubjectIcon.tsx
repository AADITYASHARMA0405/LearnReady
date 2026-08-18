import React from 'react';
import { View, Text, StyleSheet, ViewStyle } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { Colors, SubjectColors } from '../../constants/colors';
import { Typography, Spacing } from '../../constants/typography';

interface SubjectIconProps {
  id: string;
  name: string;
  iconName: keyof typeof Ionicons.glyphMap;
  size?: 'sm' | 'md' | 'lg';
  style?: ViewStyle;
}

export const SubjectIcon: React.FC<SubjectIconProps> = ({
  id,
  name,
  iconName,
  size = 'md',
  style,
}) => {
  const dimensions = size === 'sm' ? 40 : size === 'md' ? 56 : 72;
  const iconSize = size === 'sm' ? 20 : size === 'md' ? 28 : 36;
  const bgColor = SubjectColors[id] || Colors.pastelBlue;

  return (
    <View style={[styles.container, style]}>
      <View
        style={[
          styles.iconContainer,
          {
            width: dimensions,
            height: dimensions,
            borderRadius: dimensions / 2,
            backgroundColor: bgColor,
          },
        ]}
      >
        <Ionicons name={iconName} size={iconSize} color={Colors.textDark} />
      </View>
      {name && (
        <Text
          style={[styles.label, size === 'sm' && styles.labelSmall]}
          numberOfLines={2}
          ellipsizeMode="tail"
        >
          {name}
        </Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    width: 80,
  },
  iconContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: Spacing.sm,
  },
  label: {
    ...Typography.captionBold,
    textAlign: 'center',
    color: Colors.textDark,
  },
  labelSmall: {
    fontSize: 10,
    lineHeight: 14,
  },
});
