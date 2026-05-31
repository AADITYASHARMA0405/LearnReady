/**
 * SQLite Database initialization for LearnReady App
 * Opens/creates the database and runs schema migrations on first launch
 */

import * as SQLite from 'expo-sqlite';
import { CREATE_TABLES_SQL, SEED_INSTITUTIONS_SQL, SCHEMA_VERSION } from './schema';

const DB_NAME = 'learnready.db';

let db: SQLite.SQLiteDatabase | null = null;

/**
 * Get or create the database instance
 */
export const getDatabase = async (): Promise<SQLite.SQLiteDatabase> => {
  if (db) return db;

  db = await SQLite.openDatabaseAsync(DB_NAME);

  // Enable WAL mode for better performance on low-end devices
  await db.execAsync('PRAGMA journal_mode = WAL;');

  // Run schema creation (IF NOT EXISTS makes this safe to run multiple times)
  await db.execAsync(CREATE_TABLES_SQL);

  // Seed institutions if empty
  const result = await db.getFirstAsync<{ count: number }>(
    'SELECT COUNT(*) as count FROM institutions'
  );
  if (result && result.count === 0) {
    await db.execAsync(SEED_INSTITUTIONS_SQL);
  }

  console.log(`[DB] LearnReady database initialized (v${SCHEMA_VERSION})`);
  return db;
};

/**
 * Close the database connection
 */
export const closeDatabase = async () => {
  if (db) {
    await db.closeAsync();
    db = null;
  }
};

/**
 * Execute a query and return all rows
 */
export const queryAll = async <T>(sql: string, params: any[] = []): Promise<T[]> => {
  const database = await getDatabase();
  return database.getAllAsync<T>(sql, params);
};

/**
 * Execute a query and return the first row
 */
export const queryFirst = async <T>(sql: string, params: any[] = []): Promise<T | null> => {
  const database = await getDatabase();
  return database.getFirstAsync<T>(sql, params);
};

/**
 * Execute a write query (INSERT, UPDATE, DELETE)
 */
export const execute = async (sql: string, params: any[] = []) => {
  const database = await getDatabase();
  return database.runAsync(sql, params);
};

/**
 * Execute multiple statements in a transaction
 */
export const executeTransaction = async (queries: { sql: string; params?: any[] }[]) => {
  const database = await getDatabase();
  await database.withTransactionAsync(async () => {
    for (const q of queries) {
      await database.runAsync(q.sql, q.params || []);
    }
  });
};
