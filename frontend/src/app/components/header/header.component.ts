import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../shared/services/auth.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { AuthenticationResponseDto, User } from '../../shared/interfaces/user';
import { RouterModule } from '@angular/router';

import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-header',
    imports: [
    CommonModule, 
    RouterModule, 
    MatMenuModule, 
    MatButtonModule, 
    MatIconModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {
  authService = inject(AuthService);

  isUserLoggedIn = false;
  user: {firstname: string; lastname: string} | null = null;
  dropdownOpen = false;

  private sub?: Subscription;

  constructor() {}

  ngOnInit() {
    this.sub = this.authService.isLoggedIn$.subscribe((loggedIn) => {
      this.isUserLoggedIn = loggedIn;
      this.user = this.authService.getCurrentUser(); // Or use another method to fetch user info
    });
  }

  ngOnDestroy() {
    this.sub?.unsubscribe();
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  logout() {
    this.authService.logout();
    this.dropdownOpen = false;
  }
}