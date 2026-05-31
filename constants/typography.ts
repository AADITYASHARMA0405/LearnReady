/**
 * Typography scale for the app
 * Uses system Roboto — no custom fonts to keep APK small
 */

import { StyleSheet } from 'react-native';
import { Colors } from './colors';

export const Typography = StyleSheet.create({
  // Headings
  headingXL: {
    fontSize: 28,
    fontWeight: '700' as const,
    color: Colors.textDark,
    lineHeight: 36,
  },
  headingLG: {
    fontSize: 24,
    fontWeight: '700' as const,
    color: Colors.textDark,
    lineHeight: 32,
  },
  headingMD: {
    fontSize: 20,
    fontWeight: '600' as const,
    color: Colors.textDark,
    lineHeight: 28,
  },
  headingSM: {
    fontSize: 16,
    fontWeight: '600' as const,
    color: Colors.textDark,
    lineHeight: 24,
  },

  // Body
  body: {
    fontSize: 14,
    fontWeight: '400' as const,
    color: Colors.textPrimary,
    lineHeight: 22,
  },
  bodyBold: {
    fontSize: 14,
    fontWeight: '600' as const,
    color: Colors.textPrimary,
    lineHeight: 22,
  },

  // Caption
  caption: {
    fontSize: 12,
    fontWeight: '400' as const,
    color: Colors.textSecondary,
    lineHeight: 18,
  },
  captionBold: {
    fontSize: 12,
    fontWeight: '600' as const,
    color: Colors.textSecondary,
    lineHeight: 18,
  },

  // Button text
  button: {
    fontSize: 16,
    fontWeight: '600' as const,
    color: Colors.textWhite,
    lineHeight: 24,
  },

  // Label
  label: {
    fontSize: 14,
    fontWeight: '500' as const,
    color: Colors.textSecondary,
    lineHeight: 20,
  },
});

export const Spacing = {
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 20,
  xxl: 24,
  xxxl: 32,
} as const;

export const BorderRadius = {
  sm: 8,
  md: 12,
  lg: 16,
  xl: 24,
  full: 9999,
} as const;
