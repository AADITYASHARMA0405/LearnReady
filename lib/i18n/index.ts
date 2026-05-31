/**
 * i18n setup — English and Kannada bilingual support
 * Uses i18next with react-i18next for React Native
 */

import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import en from './en.json';
import kn from './kn.json';

const resources = {
  en: { translation: en },
  kn: { translation: kn },
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: 'en', // default language
    fallbackLng: 'en',
    interpolation: {
      escapeValue: false, // React already handles escaping
    },
    // Flatten nested keys with dot notation
    keySeparator: '.',
    // Don't use namespace separator
    nsSeparator: false,
  });

export default i18n;

/**
 * Helper to toggle between English and Kannada
 */
export const toggleLanguage = () => {
  const currentLang = i18n.language;
  const newLang = currentLang === 'en' ? 'kn' : 'en';
  i18n.changeLanguage(newLang);
  return newLang;
};

/**
 * Get the current language
 */
export const getCurrentLanguage = () => i18n.language;

/**
 * Check if current language is Kannada
 */
export const isKannada = () => i18n.language === 'kn';
