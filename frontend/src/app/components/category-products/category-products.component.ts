import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../shared/services/product.service';
import { Product } from '../../shared/interfaces/product';
import { CartService } from '../../shared/services/cart.service';
import { AuthService } from '../../shared/services/auth.service';

import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BackButtonComponent } from '../tools/back-button/back-button.component';

@Component({
  selector: 'app-category-products',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    BackButtonComponent
  ],
  templateUrl: './category-products.component.html',
  styleUrl: './category-products.component.css'
})
export class CategoryProductsComponent implements OnInit {
  snackbar = inject(MatSnackBar);
  authService = inject(AuthService);
  products: Product[] = [];
  categoryId!: number;
  displayedColumns = ['name', 'description', 'price', 'quantity', 'actions'];

  popupOpen: { [uuid: string]: boolean } = {};
  selectedQuantities: { [uuid: string]: number } = {};

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.categoryId = +this.route.snapshot.paramMap.get('id')!;
    this.productService.getAllProductsUnpaged().subscribe((products) => {
      this.products = products.filter((p) => p.category.id === this.categoryId);
    });
  }

  togglePopup(uuid: string): void {
    this.popupOpen[uuid] = !this.popupOpen[uuid];
    if (this.popupOpen[uuid] && !this.selectedQuantities[uuid]) {
      this.selectedQuantities[uuid] = 1;
    }
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

  // Close popup after adding
  this.popupOpen[product.uuid] = false;
  }
}
