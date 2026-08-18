git config user.name "Aaditya Sharma"
git config user.email "aadityasharma0405@example.com"
git remote remove origin 2>$null
git remote add origin https://github.com/AADITYASHARMA0405/LearnReady.git

# 20+ commits
git add .gitignore
git commit -m "Update gitignore to exclude screenshots and unnecessary documents"

git add app.json
git commit -m "Configure app settings in app.json"

git rm app/(tabs)/index.tsx 2>$null
git commit -m "Remove obsolete index tab screen"

git rm app/(tabs)/two.tsx 2>$null
git commit -m "Remove redundant tab screen"

git rm app/+html.tsx 2>$null
git commit -m "Delete unused html layout"

git rm app/+not-found.tsx 2>$null
git commit -m "Drop old not-found handler"

git rm app/modal.tsx 2>$null
git commit -m "Remove unused modal component"

git add package.json package-lock.json
git commit -m "Update project dependencies and package lock"

git add tsconfig.json
git commit -m "Configure typescript compiler options"

git add metro.config.js
git commit -m "Add metro bundler configuration"

git add constants/colors.ts
git commit -m "Define centralized color constants"

git add lib/db/database.ts
git commit -m "Implement local database schema and connection"

git add lib/db/seed-content.ts
git commit -m "Provide initial seed content for the database"

git add lib/i18n/en.json
git commit -m "Add English localization strings"

git add lib/i18n/kn.json
git commit -m "Add Kannada localization strings"

git add components/ui/Card.tsx
git commit -m "Create reusable card UI component"

git add components/ui/CircularProgress.tsx
git commit -m "Implement circular progress indicator component"

git add components/ui/EmptyState.tsx
git commit -m "Add empty state component for empty lists"

git add components/ui/Input.tsx
git commit -m "Create custom input UI component"

git add components/ui/LanguageToggle.tsx
git commit -m "Implement language toggle switch component"

git add components/ui/ProgressBar.tsx
git commit -m "Add linear progress bar component"

git add components/ui/QuizOption.tsx
git commit -m "Create quiz option selection component"

git add components/ui/SubjectIcon.tsx
git commit -m "Add subject icon rendering component"

git add components/ui/TabPills.tsx
git commit -m "Implement tab pills navigation component"

git add hooks/
git commit -m "Integrate custom React hooks for app logic"

git add app/_layout.tsx
git commit -m "Update root layout routing configuration"

git add app/(tabs)/_layout.tsx
git commit -m "Configure bottom tabs layout"

git add app/index.tsx
git commit -m "Add main entry screen"

git add app/(tabs)/home.tsx
git commit -m "Implement home tab screen"

git add app/(tabs)/learn.tsx
git commit -m "Create learning modules tab screen"

git add app/(tabs)/interview.tsx
git commit -m "Add interview preparation tab screen"

git add app/(tabs)/skills.tsx
git commit -m "Implement skills tracking tab screen"

git add app/(tabs)/profile.tsx
git commit -m "Create user profile tab screen"

git add app/(onboarding)/
git commit -m "Implement user onboarding flow"

git add app/diagnostic/
git commit -m "Add diagnostic assessment screens"

git add app/lesson/
git commit -m "Create interactive lesson screens"

git add app/mock-interview/
git commit -m "Implement mock interview experience"

git add app/quiz/
git commit -m "Add quiz assessment module"

git add app/skill/
git commit -m "Create individual skill detail screens"

git add app/subject/
git commit -m "Add subject category screens"

git add .
git commit -m "Include remaining project files and assets"

git branch -M main
git push -u origin main
