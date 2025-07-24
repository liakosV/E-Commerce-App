import { Component, inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    RouterModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  auth: AuthService = inject(AuthService)
  snackbar = inject(MatSnackBar);
  router = inject(Router);

  username = '';
  password = '';

  onSubmit() {
    this.auth.login({username: this.username, password: this.password}).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => {
        const message = err?.error?.description || err?.error?.error;
        this.snackbar.open('Login failed! Reason: ' +  message, "Close", {duration:5000});
        console.error("ERROR" , err);
         
      }
    });
  }

}
