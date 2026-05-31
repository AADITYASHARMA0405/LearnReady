/**
 * SQLite Database Schema for LearnReady App
 * All tables defined here — runs on first app launch
 */

export const SCHEMA_VERSION = 1;

export const CREATE_TABLES_SQL = `
-- ═══════════════════════════════════════════
-- USERS & INSTITUTIONS
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS institutions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  location TEXT,
  tier TEXT DEFAULT 'III',
  state TEXT DEFAULT 'Karnataka'
);

CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  phone TEXT,
  pin_hash TEXT,
  role TEXT CHECK(role IN ('student','faculty')) DEFAULT 'student',
  institution_id INTEGER REFERENCES institutions(id),
  stream TEXT,
  semester INTEGER,
  group_type TEXT CHECK(group_type IN ('intervention','control')) DEFAULT 'intervention',
  gender TEXT,
  is_first_gen_learner INTEGER DEFAULT 0,
  language_pref TEXT DEFAULT 'en',
  created_at TEXT DEFAULT (datetime('now'))
);

-- ═══════════════════════════════════════════
-- LEARNING CONTENT (bilingual EN + KN)
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS subjects (
  id INTEGER PRIMARY KEY,
  name_en TEXT NOT NULL,
  name_kn TEXT,
  stream TEXT,
  icon_name TEXT,
  color TEXT
);

CREATE TABLE IF NOT EXISTS modules (
  id INTEGER PRIMARY KEY,
  subject_id INTEGER REFERENCES subjects(id),
  title_en TEXT NOT NULL,
  title_kn TEXT,
  description_en TEXT,
  description_kn TEXT,
  order_index INTEGER,
  type TEXT CHECK(type IN ('lesson','quiz','skill','lab')),
  difficulty TEXT CHECK(difficulty IN ('easy','medium','hard')),
  prerequisites TEXT
);

CREATE TABLE IF NOT EXISTS lessons (
  id INTEGER PRIMARY KEY,
  module_id INTEGER REFERENCES modules(id),
  title_en TEXT,
  title_kn TEXT,
  content_en TEXT,
  content_kn TEXT,
  key_points_en TEXT,
  key_points_kn TEXT,
  order_index INTEGER
);

CREATE TABLE IF NOT EXISTS questions (
  id INTEGER PRIMARY KEY,
  module_id INTEGER REFERENCES modules(id),
  subject_id INTEGER REFERENCES subjects(id),
  type TEXT CHECK(type IN ('mcq','true_false','fill_blank')),
  question_en TEXT NOT NULL,
  question_kn TEXT,
  options_en TEXT,
  options_kn TEXT,
  correct_answer TEXT,
  explanation_en TEXT,
  explanation_kn TEXT,
  difficulty INTEGER DEFAULT 2
);

-- ═══════════════════════════════════════════
-- STUDENT PROGRESS & LEARNING
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS student_progress (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  module_id INTEGER REFERENCES modules(id),
  status TEXT CHECK(status IN ('locked','available','in_progress','completed')) DEFAULT 'locked',
  score REAL DEFAULT 0,
  best_score REAL DEFAULT 0,
  attempts INTEGER DEFAULT 0,
  time_spent_seconds INTEGER DEFAULT 0,
  mastery_level REAL DEFAULT 0,
  last_accessed TEXT,
  completed_at TEXT
);

CREATE TABLE IF NOT EXISTS learning_paths (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  path_data TEXT,
  weak_areas TEXT,
  generated_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS quiz_attempts (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  question_id INTEGER REFERENCES questions(id),
  selected_answer TEXT,
  is_correct INTEGER,
  time_taken_seconds INTEGER,
  difficulty_at_attempt INTEGER,
  attempted_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS review_schedule (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  question_id INTEGER REFERENCES questions(id),
  next_review_at TEXT,
  interval_days INTEGER DEFAULT 1,
  ease_factor REAL DEFAULT 2.5,
  repetitions INTEGER DEFAULT 0
);

-- ═══════════════════════════════════════════
-- EMPLOYABILITY & SKILLS
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS job_readiness (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  digital_literacy_score REAL DEFAULT 0,
  communication_score REAL DEFAULT 0,
  problem_solving_score REAL DEFAULT 0,
  interview_score REAL DEFAULT 0,
  technical_score REAL DEFAULT 0,
  overall_score REAL DEFAULT 0,
  updated_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS skill_scores (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  skill_type TEXT CHECK(skill_type IN ('digital_literacy','communication','problem_solving','interview_skills')),
  module_title TEXT,
  score REAL,
  max_score REAL,
  assessed_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS interview_attempts (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  category TEXT CHECK(category IN ('hr','technical')),
  questions_asked TEXT,
  student_responses TEXT,
  ai_scores TEXT,
  overall_score REAL,
  feedback TEXT,
  attempted_at TEXT DEFAULT (datetime('now'))
);

-- ═══════════════════════════════════════════
-- ASSESSMENTS
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS assessments (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  type TEXT CHECK(type IN ('diagnostic','pre_test','post_test','employability')),
  total_score REAL,
  max_score REAL,
  responses TEXT,
  started_at TEXT,
  completed_at TEXT
);

-- ═══════════════════════════════════════════
-- TAM SURVEY (Research)
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS tam_surveys (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  survey_phase TEXT CHECK(survey_phase IN ('pre','mid','post')),
  pu_1 INTEGER, pu_2 INTEGER, pu_3 INTEGER, pu_4 INTEGER,
  peou_1 INTEGER, peou_2 INTEGER, peou_3 INTEGER, peou_4 INTEGER,
  atu_1 INTEGER, atu_2 INTEGER, atu_3 INTEGER,
  bi_1 INTEGER, bi_2 INTEGER, bi_3 INTEGER,
  dse_1 INTEGER, dse_2 INTEGER, dse_3 INTEGER,
  qualitative_feedback TEXT,
  submitted_at TEXT DEFAULT (datetime('now'))
);

-- ═══════════════════════════════════════════
-- PLATFORM ANALYTICS LOGGING
-- ═══════════════════════════════════════════

CREATE TABLE IF NOT EXISTS platform_logs (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER REFERENCES users(id),
  event_type TEXT,
  event_data TEXT,
  duration_seconds INTEGER,
  timestamp TEXT DEFAULT (datetime('now')),
  synced INTEGER DEFAULT 0
);
`;

// Seed data for demo institutions
export const SEED_INSTITUTIONS_SQL = `
INSERT OR IGNORE INTO institutions (id, name, location, tier, state) VALUES
  (1, 'Government Engineering College, Raichur', 'Raichur', 'III', 'Karnataka'),
  (2, 'Rural Engineering College, Bagalkot', 'Bagalkot', 'III', 'Karnataka'),
  (3, 'District Engineering Institute, Kolar', 'Kolar', 'III', 'Karnataka'),
  (4, 'Tier-III Engineering College, Haveri', 'Haveri', 'III', 'Karnataka'),
  (5, 'Government Polytechnic, Chitradurga', 'Chitradurga', 'III', 'Karnataka');
`;
