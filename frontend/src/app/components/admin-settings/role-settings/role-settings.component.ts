import { Component, inject, OnInit } from '@angular/core';
import { RoleService } from '../../../shared/services/role.service';
import { Role, RoleInsertDto } from '../../../shared/interfaces/role';

import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-role-settings',
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './role-settings.component.html',
  styleUrl: './role-settings.component.css'
})
export class RoleSettingsComponent implements OnInit {
  roleService = inject(RoleService);
  snackbar = inject(MatSnackBar);

  roles: Role[] = [];

  roleInsert: RoleInsertDto = {
    name: '',
    description: ''
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles() {
    this.roleService.getAllRoles().subscribe({
      next: (data) => this.roles = data,
      error: () => {
        this.snackbar.open("Failed to load roles", "Close", {duration: 5000});
      }
    })
  }

  createRole() {
    if (!this.roleInsert.name.trim()) {
      return;
    }

    this.roleService.createRole(this.roleInsert).subscribe({
      next: () => {
        this.snackbar.open("Role created", "Close", {duration: 3000});
        this.roleInsert.name = '';
        this.roleInsert.description = '';
        this.loadRoles();
      },
      error: (err) => {
        this.snackbar.open("Failed. " + err.error.description , "Close", {duration: 5000});
      }
    });
  }

  removeRole(id: number) {
    this.roleService.removeRole(id).subscribe({
      next: () => {
        this.snackbar.open("Role removed", "Close", {duration: 3000});
        this.loadRoles();
      },
      error: (err) => {
        this.snackbar.open("Failed to remove role. " + err.error.description, "Close", {duration: 5000});
      }
    })
  }

}
