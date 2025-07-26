import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupName, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../shared/services/product.service';
import { Page } from '../../shared/interfaces/page';
import { Product } from '../../shared/interfaces/product';
import { ProductFilters } from '../../shared/interfaces/product-filters';
import { CartService } from '../../shared/services/cart.service';
import { AuthService } from '../../shared/services/auth.service';

import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTableModule } from '@angular/material/table';
import { Category } from '../../shared/interfaces/category';
import { MatSelectModule } from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';
import { CategoryService } from '../../shared/services/category.service';

@Component({
  selector: 'app-product-list',
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSortModule,
    MatTooltipModule,
    MatTableModule,
    MatSelectModule,
    MatRadioModule,
    ReactiveFormsModule,
    FormsModule,
],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  fb = inject(FormBuilder);
  productService = inject(ProductService);
  authService = inject(AuthService);
  cartService = inject(CartService);
  categoryService = inject(CategoryService);
  snackbar = inject(MatSnackBar);

  filterForm!: FormGroup;
  productPage!: Page<Product>;
  categories: Category[] = [];
  page = 0;
  size = 10;
  sortBy = '';
  sortDirection = 'asc';

  popupOpen: { [productUuid: string]: boolean } = {};
  selectedQuantities: { [productUuid: string]: number } = {};
  submittedFilters: ProductFilters = {};

  get products(): Product[] {
    return this.productPage?.content || [];
  }

  get totalPages(): number {
    return this.productPage?.totalPages || 0;
  }

  ngOnInit(): void {
    this.filterForm = this.fb.group({
      category: [null as Category | null],
      minPrice: ['', Validators.min(0)],
      maxPrice: ['', Validators.min(0)],
      isActive: [true],
      search: ['']
    });

    this.loadCategories();
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService
    .getProductsFiltered({...this.submittedFilters}, this.page, this.size, this.sortBy, this.sortDirection)
    .subscribe(page => this.productPage = page);
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories = data,
      error: () => this.snackbar.open("Failed to load categories", "Close", {duration:5000})
    })
  }



  onSubmit(): void {
    this.page = 0;
    this.submittedFilters = {...this.filterForm.value};
    this.loadProducts();
  }

  removeProduct(product: Product) {
    this.productService.removeProduct(product.uuid).subscribe({
      next:() => {
        this.snackbar.open("Product has been removed", "Close", {duration: 3000});
        this.loadProducts();
      },
      error: (err) => {
        this.snackbar.open("Failed to remove a product", "Close", {duration: 5000});
      }
    })
  }

  togglePopup(productUuid: string): void {
    this.popupOpen[productUuid] = !this.popupOpen[productUuid];
    this.selectedQuantities[productUuid] = 1;
  }

  addToCart(product: Product): void {
    const selectedQuantity = this.selectedQuantities[product.uuid];

    if (!selectedQuantity || selectedQuantity < 1 || selectedQuantity % 1 !== 0) {
      this.snackbar.open('Invalid quantity selected', 'Close', { duration: 3000 });
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

  clearFilters() {
    this.filterForm.reset({
      category: null,
      minPrice: '',
      maxPrice: '',
      isActive: true,
      search: ''
    });
    this.loadProducts();
  }

    onPageChange(event: PageEvent) {
    this.page = event.pageIndex;
    this.size = event.pageSize
    this.loadProducts();
  }

    onSortChange(sort: Sort) {
    if (!sort.direction) return;
    this.sortBy = sort.active;
    this.sortDirection = sort.direction || 'asc';
    this.loadProducts();
  }


}
