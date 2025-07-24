import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../../shared/services/user.service';
import { User, UserReadOnlyDto } from '../../../shared/interfaces/user';
import { Page } from '../../../shared/interfaces/page';
import { Sort } from '@angular/material/sort';

import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-settings',
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent implements OnInit {
  userService = inject(UserService);
  snackbar = inject(MatSnackBar);
  router = inject(Router);

  usersPage!: Page<UserReadOnlyDto>;
  sortBy = 'username';
  sortDirection = 'asc';

  get users(): UserReadOnlyDto[] {
    return this.usersPage?.content || [];
  }

    get totalPages(): number {
    return this.usersPage?.totalPages || 0;
  }

  get page(): number {
    return this.usersPage?.number || 0;
  }

  get size(): number {
    return this.usersPage?.size || 10;
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService
    .getAllUsers(this.page, this.size, this.sortBy, this.sortDirection)
    .subscribe({
      next: (data) => this.usersPage = data,
      error: () => this.snackbar.open("Failed to load users", "Close", {duration: 5000})
    });
  }

  deactivateUser(user: UserReadOnlyDto) {
    const status = !user.isActive;
    this.userService.deactivateUser(user.uuid).subscribe({
      next: () => {
        this.snackbar.open("User status changed to " + status, "Close", {duration: 3000});
        this.loadUsers();
      },
      error: (err) => {
        this.snackbar.open("Failed to change the status of the user. Reason: " + err.error.description, "Close", {duration: 5000});
      }
    })
  }

  removeUser(user: UserReadOnlyDto) {
    const confirmed = window.confirm(`Are you sure you want to permanently delete this user: "${user.username}"?`);

    if (!confirmed) {
      return;
    }

    this.userService.removeUser(user.uuid).subscribe({
      next: () => {
        this.snackbar.open("User removed successfully", "Close", {duration: 3000});
        this.loadUsers();
      },
      error: (err) => {
        this.snackbar.open("Failed to remove user. Reason: " + err.error.description, "Close", {duration: 5000});
      }
    })
  }

  goToPersonalInfo(userId: string) {
    this.router.navigate([`/account/${userId}`]);
  }

  changePage(newPage: number) {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.usersPage.number = newPage;
      this.loadUsers();
    }
  }

  onSortChange(sort: Sort) {
    if (!sort.direction) return;
    this.sortBy = sort.active;
    this.sortDirection = sort.direction || "asc";
    this.loadUsers();
  }

}
