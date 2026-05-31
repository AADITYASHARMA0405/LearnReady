/**
 * Button component — matches reference UI
 * Full-width blue primary button, 48dp height, 12dp radius
 */

import React from 'react';
import {
  TouchableOpacity,
  Text,
  StyleSheet,
  ActivityIndicator,
  ViewStyle,
  TextStyle,
} from 'react-native';
import { Colors } from '../../constants/colors';
import { BorderRadius } from '../../constants/typography';

interface ButtonProps {
  title: string;
  onPress: () => void;
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  style?: ViewStyle;
  textStyle?: TextStyle;
  icon?: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  title,
  onPress,
  variant = 'primary',
  size = 'lg',
  disabled = false,
  loading = false,
  style,
  textStyle,
  icon,
}) => {
  const buttonStyles = [
    styles.base,
    styles[variant],
    styles[`size_${size}`],
    disabled && styles.disabled,
    style,
  ];

  const labelStyles = [
    styles.label,
    styles[`label_${variant}`],
    styles[`labelSize_${size}`],
    disabled && styles.labelDisabled,
    textStyle,
  ];

  return (
    <TouchableOpacity
      style={buttonStyles}
      onPress={onPress}
      disabled={disabled || loading}
      activeOpacity={0.7}
    >
      {loading ? (
        <ActivityIndicator
          color={variant === 'primary' ? Colors.textWhite : Colors.primary}
          size="small"
        />
      ) : (
        <>
          {icon && icon}
          <Text style={labelStyles}>{title}</Text>
        </>
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  base: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: BorderRadius.md,
    gap: 8,
  },

  // Variants
  primary: {
    backgroundColor: Colors.primary,
  },
  secondary: {
    backgroundColor: Colors.bgMain,
  },
  outline: {
    backgroundColor: 'transparent',
    borderWidth: 1.5,
    borderColor: Colors.primary,
  },
  ghost: {
    backgroundColor: 'transparent',
  },

  // Sizes
  size_sm: {
    height: 36,
    paddingHorizontal: 16,
  },
  size_md: {
    height: 42,
    paddingHorizontal: 20,
  },
  size_lg: {
    height: 48,
    paddingHorizontal: 24,
  },

  // Disabled
  disabled: {
    opacity: 0.5,
  },

  // Labels
  label: {
    fontWeight: '600',
    textAlign: 'center',
  },
  label_primary: {
    color: Colors.textWhite,
    fontSize: 16,
  },
  label_secondary: {
    color: Colors.primary,
    fontSize: 16,
  },
  label_outline: {
    color: Colors.primary,
    fontSize: 16,
  },
  label_ghost: {
    color: Colors.primary,
    fontSize: 14,
  },

  labelSize_sm: {
    fontSize: 13,
  },
  labelSize_md: {
    fontSize: 14,
  },
  labelSize_lg: {
    fontSize: 16,
  },

  labelDisabled: {
    opacity: 0.7,
  },
});
