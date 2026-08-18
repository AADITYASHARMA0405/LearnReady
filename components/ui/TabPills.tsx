import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ScrollView, ViewStyle } from 'react-native';
import { Colors } from '../../constants/colors';
import { Typography, Spacing, BorderRadius } from '../../constants/typography';

export interface TabItem {
  id: string;
  label: string;
}

interface TabPillsProps {
  tabs: TabItem[];
  activeTabId: string;
  onTabChange: (id: string) => void;
  style?: ViewStyle;
}

export const TabPills: React.FC<TabPillsProps> = ({
  tabs,
  activeTabId,
  onTabChange,
  style,
}) => {
  return (
    <View style={[styles.container, style]}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        {tabs.map((tab) => {
          const isActive = tab.id === activeTabId;
          return (
            <TouchableOpacity
              key={tab.id}
              style={[styles.pill, isActive ? styles.pillActive : styles.pillInactive]}
              onPress={() => onTabChange(tab.id)}
              activeOpacity={0.7}
            >
              <Text style={[styles.label, isActive ? styles.labelActive : styles.labelInactive]}>
                {tab.label}
              </Text>
            </TouchableOpacity>
          );
        })}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    marginBottom: Spacing.md,
  },
  scrollContent: {
    paddingHorizontal: Spacing.lg,
    gap: Spacing.sm,
  },
  pill: {
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: BorderRadius.full,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  pillActive: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
  },
  pillInactive: {
    backgroundColor: Colors.bgCard,
    borderColor: Colors.border,
  },
  label: {
    ...Typography.bodyBold,
  },
  labelActive: {
    color: Colors.textWhite,
  },
  labelInactive: {
    color: Colors.textSecondary,
  },
});
