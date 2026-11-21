import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  TotpConfirmRequest,
  TotpSetupResponse,
  TotpVerifyRequest,
  UserAuth,
} from '../models/auth.models';

const ACCESS_TOKEN_KEY = 'coo_access_token';
const USER_KEY = 'coo_user_data';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  readonly currentUser = signal<UserAuth | null>(this.getStoredUser());
  readonly accessToken = signal<string | null>(this.getStoredToken());
  readonly preAuthToken = signal<string | null>(null);

  readonly isAuthenticated = computed(() => !!this.accessToken());
  readonly tenantId = computed(() => this.currentUser()?.cooperativeId ?? null);
  readonly tenantName = computed(() => this.currentUser()?.cooperativeName ?? '');

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        if (response.is2faRequired && response.preAuthToken) {
          this.preAuthToken.set(response.preAuthToken);
        } else if (response.accessToken && response.user) {
          this.saveSession(response.accessToken, response.user);
        }
      }),
    );
  }

  verifyTwoFactor(request: TotpVerifyRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/2fa/verify`, request).pipe(
      tap((response) => {
        if (response.accessToken && response.user) {
          this.saveSession(response.accessToken, response.user);
          this.preAuthToken.set(null);
        }
      }),
    );
  }

  setupTwoFactor(): Observable<TotpSetupResponse> {
    return this.http.post<TotpSetupResponse>(`${this.apiUrl}/2fa/setup`, {});
  }

  confirmTwoFactor(request: TotpConfirmRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/2fa/confirm`, request).pipe(
      tap(() => {
        const user = this.currentUser();
        if (user) {
          const updated = { ...user, twoFactorEnabled: true };
          this.currentUser.set(updated);
          sessionStorage.setItem(USER_KEY, JSON.stringify(updated));
        }
      }),
    );
  }

  disableTwoFactor(code: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/2fa/disable?code=${code}`, {}).pipe(
      tap(() => {
        const user = this.currentUser();
        if (user) {
          const updated = { ...user, twoFactorEnabled: false };
          this.currentUser.set(updated);
          sessionStorage.setItem(USER_KEY, JSON.stringify(updated));
        }
      }),
    );
  }

  getMe(): Observable<UserAuth> {
    return this.http.get<UserAuth>(`${this.apiUrl}/me`).pipe(
      tap((user) => {
        this.currentUser.set(user);
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));
      }),
    );
  }

  saveSession(token: string, user: UserAuth): void {
    this.accessToken.set(token);
    this.currentUser.set(user);
    sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  logout(): void {
    this.accessToken.set(null);
    this.currentUser.set(null);
    this.preAuthToken.set(null);
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);
    sessionStorage.removeItem(USER_KEY);
    this.router.navigate(['/login']);
  }

  hasPermission(permission: string): boolean {
    const user = this.currentUser();
    if (!user || !user.roles) {
      return false;
    }
    const isAdmin = user.roles.some((role) => {
      const name = role.name ? role.name.toLowerCase() : '';
      return name === 'administrador' || name === 'admin';
    });
    if (isAdmin) {
      return true;
    }
    return user.roles.some((role) =>
      role.permissions ? role.permissions.includes(permission) : false,
    );
  }

  hasRole(roleName: string): boolean {
    const user = this.currentUser();
    if (!user || !user.roles) {
      return false;
    }
    return user.roles.some((role) => role.name.toLowerCase() === roleName.toLowerCase());
  }

  getGoogleLoginUrl(): string {
    return `${environment.apiUrl}/oauth2/authorization/google`;
  }

  private getStoredToken(): string | null {
    try {
      return sessionStorage.getItem(ACCESS_TOKEN_KEY);
    } catch {
      return null;
    }
  }

  private getStoredUser(): UserAuth | null {
    try {
      const data = sessionStorage.getItem(USER_KEY);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  }
}

