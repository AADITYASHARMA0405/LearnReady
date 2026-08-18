import { create } from 'zustand';
import { queryFirst } from '../lib/db/database';

interface User {
  id: number;
  name: string;
  role: string;
  phone?: string;
  email?: string;
  full_name?: string;
  diagnostic_completed?: number;
  diagnostic_score?: number;
}

interface AuthState {
  user: User | null;
  isLoading: boolean;
  checkAuth: () => Promise<void>;
  signIn: (user: User) => void;
  signOut: () => void;
  login: (user: User) => void;
  logout: () => void;
}

export const useAuth = create<AuthState>((set) => ({
  user: null,
  isLoading: true,
  checkAuth: async () => {
    try {
      // In a real app we'd use secure storage for a session token.
      // For this offline demo, we just check if any user exists in the DB.
      // We log in the first user we find.
      const user = await queryFirst<User>('SELECT id, name, role FROM users LIMIT 1');
      set({ user: user || null, isLoading: false });
    } catch (error) {
      console.error('Check auth error:', error);
      set({ user: null, isLoading: false });
    }
  },
  signIn: (user) => set({ user }),
  signOut: () => set({ user: null }),
  login: (user) => set({ user }),
  logout: () => set({ user: null }),
}));
