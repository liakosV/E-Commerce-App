import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../../shared/interfaces/user';
import { RoleService } from '../../shared/services/role.service';
import { Role } from '../../shared/interfaces/role';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  auth = inject(AuthService);
  roleService = inject(RoleService);
  router = inject(Router);
  snackBar = inject(MatSnackBar)

  user: User = {
    username: '',
    firstname: '',
    lastname: '',
    password: '',
    email: '',
    roleName: '',
  };

  roles: Role[] = []

  ngOnInit(): void {
    this.roleService.getAllRoles().subscribe({
      next: (roles) => {
        this.roles = roles;

        // This is for filtering the ADMIN role to not show in the dropbox.
        // this.roles = roles.filter(role => role.name !== "ADMIN");
      },
      error: (err) => {
        console.error("Failed to load roles", err);
        this.snackBar.open("Failed to load roles", "Close", {duration: 5000});
      }
    })
  }

  onSubmit() {
    this.auth.register(this.user).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        const errorObj = err?.error;
        
        if (errorObj && typeof errorObj === 'object') {
          const messages = Object.values(errorObj).join(', \n');
          this.snackBar.open(errorObj.description || messages, 'Close', {
            duration: 10000
          })
          console.error( errorObj.description || messages);
          
        } else {
          alert('Registration failed. Please try again.');
        }
      }
    });
  }
}

