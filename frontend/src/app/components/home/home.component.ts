import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/services/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { ProductListComponent } from "../product-list/product-list.component";


@Component({
  selector: 'app-home',
    imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatButtonModule,
    ProductListComponent
],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  authService = inject(AuthService);

  get isAdminOrSeller(): boolean {
    const role = this.authService.getUserRole();
    return role === 'ADMIN' || role === 'SELLER';
  }
}
