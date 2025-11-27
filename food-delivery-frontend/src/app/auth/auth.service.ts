import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:9999/auth';

  token = signal<string | null>(null);
  role = signal<string | null>(null);
  name = signal<string | null>(null);

  constructor(private http: HttpClient) { }

  // Login API
  login(data: any) {
    return this.http.post(`${this.baseUrl}/login`, data, { responseType: 'text' });
  }

  // Register API
  register(data: any) {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  saveSession(token: string) {
    console.log('TOKEN STRING =', token);

    const decoded: any = jwtDecode(token);

    localStorage.setItem('token', token);
    localStorage.setItem('role', decoded.role);
    localStorage.setItem('name', decoded.sub);
    localStorage.setItem('userId', decoded.id);

    this.token.set(token);
    this.role.set(decoded.role);
    this.name.set(decoded.sub);
  }

  // Logout
  logout() {
    localStorage.clear();
    this.token.set(null);
    this.role.set(null);
    this.name.set(null);
  }

  // Get JWT token
  getToken(): string | null {
    return this.token() || localStorage.getItem('token');
  }

  // Get user role
  getRole(): string | null {
    return this.role() || localStorage.getItem('role');
  }

  // Get user name
  getName(): string | null {
    return this.name() || localStorage.getItem('name');
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
