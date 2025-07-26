import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { AuthenticationResponseDto, Credentials, User } from '../interfaces/user';
import { jwtDecode } from 'jwt-decode';
import { CartService } from './cart.service';

const API_URL_AUTH = `${environment.apiURL}/api/auth/authenticate`
const API_URL = `${environment.apiURL}/api/users`
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  http: HttpClient = inject(HttpClient);
  cartService = inject(CartService);

  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor() {}

  getUserId(): string | null  {
    const decoded = this.getDecodedToken();
    return decoded?.uuid || null;
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

login(credentials: Credentials): Observable<any> {
  return this.http.post<AuthenticationResponseDto>(API_URL_AUTH, credentials).pipe(
    tap(res => {
      localStorage.setItem('token', res.token);
      localStorage.setItem('firstname', res.firstname);
      localStorage.setItem('lastname', res.lastname);
      this.isLoggedInSubject.next(true);
    })
  );
}

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('firstname');
    localStorage.removeItem('lastname');
    this.isLoggedInSubject.next(false);
    this.cartService.clearCart();
  }

  register(user: User) {
    return this.http.post(`${API_URL}`, user)
  }

  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }

  getCurrentUser(): { firstname: string; lastname: string } | null {
    const firstname = localStorage.getItem('firstname');
    const lastname = localStorage.getItem('lastname');
    if (firstname && lastname) {
      return { firstname, lastname };
    }
    return null;
  }

  getDecodedToken(): any | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
    try {
      return jwtDecode(token);
    } catch (error) {
      return null;
    }
  }

  getUserRole(): string | null {
    const decoded = this.getDecodedToken();
    return decoded?.role || null;
  }

  hasRole(role: string): boolean {
    const userRole = this.getUserRole();
    return userRole === role;
  }

  hasAnyRole(roles: string[]): boolean {
    const userRole = this.getUserRole();
    return roles.includes(userRole || '');
  }

  isTokenExpired(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return true;

    try {
      const decoded = jwtDecode(token);
      const exp = decoded.exp;
      const now = Math.floor(Date.now() / 1000);

      if (exp) {
        return exp < now;
      } else {
        return true;
      }
    } catch (error) {
      return true;
    }
  }
}
