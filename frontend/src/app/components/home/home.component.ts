import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../shared/interfaces/product';
import { ProductService } from '../../shared/services/product.service';
import { AuthService } from '../../shared/services/auth.service';
import { CartService } from '../../shared/services/cart.service';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSort, Sort } from '@angular/material/sort';
import { RouterModule } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-home',
    imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatSortModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  @ViewChild(MatSort) sort!: MatSort;
  authService = inject(AuthService);
  cartService = inject(CartService)
  snackbar = inject(MatSnackBar);

  displayedColumns: string[] = ['name', 'price', 'description', 'quantity', 'isActive'];
  popupOpen: { [productUuid: string]: boolean } = {};
  selectedQuantities: { [productUuid: string]: number } = {};
  products: Product[] = [];
  totalPages = 0;
  page = 0;
  size = 10;

  sortBy = 'name';
  sortDirection = 'asc';

  constructor(private productService: ProductService) {
    this.loadProducts();
  }

  ngOnInit(): void {
    this.displayedColumns.push('actions');
  }

  get isAdminOrSeller(): boolean {
    const role = this.authService.getUserRole();
    return role === 'ADMIN' || role === 'SELLER'
  }

togglePopup(productUuid: string): void {
  this.popupOpen[productUuid] = !this.popupOpen[productUuid];
  this.selectedQuantities[productUuid] = 1;
}

addToCart(product: Product): void {
  const selectedQuantity = this.selectedQuantities[product.uuid];

  if(!selectedQuantity || selectedQuantity < 1 || selectedQuantity % 1 !== 0) {
    this.snackbar.open('Invalid quantity selected', 'Close', {duration: 3000});
    return;
  }

  if (product.quantity === 0) {
    this.snackbar.open('Product is out of stock', 'Close', { duration: 3000 });
    return;
  }

  if (selectedQuantity > product.quantity) {
    this.snackbar.open('Quantity exceeds available stock', 'Close', { duration: 3000 });
    return;
  }

  this.cartService.addItem({
    productUuid: product.uuid,
    name: product.name,
    price: product.price,
    quantity: selectedQuantity,
  });

  this.snackbar.open('Added to cart', 'Close', { duration: 2000 });

  this.popupOpen[product.uuid] = false;
  }

  loadProducts(): void {
    this.productService
      .getProducts(this.page, this.size, this.sortBy, this.sortDirection)
      .subscribe((res) => {
        this.products = res.content;
        this.totalPages = res.totalPages;
      });
  }

  changePage(newPage: number) {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.page = newPage;
      this.loadProducts();
    }
  }
  onSortChange(sort: Sort) {
    if(!sort.direction) return;
    this.sortBy = sort.active;
    this.sortDirection = sort.direction || 'asc';
    this.loadProducts();
  }
}
