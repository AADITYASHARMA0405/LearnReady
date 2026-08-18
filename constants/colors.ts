/**
 * Color palette for LearnReady App
 * Light-blue / pastel design matching the reference screenshots
 */

export const Colors = {
  // Primary
  primary: '#3B82F6',        // Main blue
  primaryDark: '#2563EB',    // Darker blue for pressed states
  primaryLight: '#DBEAFE',   // Very light blue for pill backgrounds

  // Backgrounds
  bgMain: '#F8FAFC',         // Off-white page background
  bgCard: '#FFFFFF',         // White card surface
  bgInput: '#F1F5F9',        // Light grey input fields

  // Text
  textDark: '#1E293B',       // Headings
  textPrimary: '#334155',    // Body text
  textSecondary: '#64748B',  // Subtitles, labels
  textHint: '#94A3B8',       // Placeholder text
  textWhite: '#FFFFFF',      // Text on primary buttons

  // Borders
  border: '#E2E8F0',         // Default border
  borderError: '#EF4444',    // Error border

  // Status
  success: '#22C55E',
  warning: '#F59E0B',
  error: '#EF4444',

  // Pastel Subject Icons
  pastelBlue: '#BFDBFE',
  pastelPurple: '#E9D5FF',
  pastelGreen: '#BBF7D0',
  pastelOrange: '#FED7AA',
  pastelPink: '#FECDD3',
  pastelYellow: '#FEF08A',

  // Gradient
  gradientStart: '#6366F1',
  gradientEnd: '#8B5CF6',

  // Shadow
  shadow: '#000000',
} as const;

/**
 * Map subject IDs to pastel background colors
 */
export const SubjectColors: Record<string, string> = {
  math: Colors.pastelBlue,
  cs: Colors.pastelPurple,
  physics: Colors.pastelGreen,
  mech: Colors.pastelOrange,
  civil: Colors.pastelPink,
  eee: Colors.pastelYellow,
  ece: Colors.pastelBlue,
  default: Colors.pastelBlue,
};
