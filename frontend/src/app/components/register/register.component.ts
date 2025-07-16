import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../../shared/interfaces/user';

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
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  auth = inject(AuthService);
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

  onSubmit() {
    this.auth.register(this.user).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        const errorObj = err?.error;
        
        if (errorObj && typeof errorObj === 'object') {
          const messages = Object.values(errorObj).join(',\n');
          this.snackBar.open(messages, 'Close', {
            duration: 10000
          })
        } else {
          alert('Registration failed. Please try again.');
        }
      }
    });
  }
}

