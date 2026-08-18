const { getDefaultConfig } = require('expo/metro-config');

/** @type {import('expo/metro-config').MetroConfig} */
const config = getDefaultConfig(__dirname);

// Add 'wasm' to the list of file extensions that Metro considers as assets
// This is required for expo-sqlite to work correctly on the web
config.resolver.assetExts.push('wasm');

module.exports = config;
