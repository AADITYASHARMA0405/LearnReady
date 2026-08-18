import React from 'react';
import { View, StyleSheet } from 'react-native';
import Svg, { Circle, Text as SvgText, G } from 'react-native-svg';
import { Colors } from '../../constants/colors';

interface CircularProgressProps {
  score: number; // 0 to 100
  size?: number;
  strokeWidth?: number;
  label?: string;
}

export const CircularProgress: React.FC<CircularProgressProps> = ({
  score,
  size = 80,
  strokeWidth = 8,
  label,
}) => {
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const safeScore = Math.min(Math.max(score, 0), 100);
  const strokeDashoffset = circumference - (safeScore / 100) * circumference;

  // Blue if >= 50%, Red if < 50%
  const progressColor = safeScore >= 50 ? Colors.primary : Colors.error;
  const bgColor = Colors.border;
  const center = size / 2;

  return (
    <View style={styles.container}>
      <Svg width={size} height={size}>
        <G rotation="-90" origin={`${center}, ${center}`}>
          {/* Background Circle */}
          <Circle
            cx={center}
            cy={center}
            r={radius}
            stroke={bgColor}
            strokeWidth={strokeWidth}
            fill="transparent"
          />
          {/* Progress Circle */}
          <Circle
            cx={center}
            cy={center}
            r={radius}
            stroke={progressColor}
            strokeWidth={strokeWidth}
            fill="transparent"
            strokeDasharray={circumference}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
          />
        </G>
        {/* Score Text */}
        <SvgText
          x={center}
          y={center + (label ? -4 : 4)} // Adjust vertically if there's a label below
          fontSize={size * 0.25}
          fontWeight="bold"
          fill={Colors.textDark}
          textAnchor="middle"
          alignmentBaseline="middle"
        >
          {`${Math.round(safeScore)}%`}
        </SvgText>
        {/* Optional Label */}
        {label && (
          <SvgText
            x={center}
            y={center + size * 0.2}
            fontSize={size * 0.12}
            fill={Colors.textSecondary}
            textAnchor="middle"
            alignmentBaseline="middle"
          >
            {label}
          </SvgText>
        )}
      </Svg>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
  },
});
