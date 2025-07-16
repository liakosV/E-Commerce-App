import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../../shared/interfaces/product';
import { ProductService } from '../../shared/services/product.service';

import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSort, Sort } from '@angular/material/sort';
@Component({
  selector: 'app-home',
    imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['name', 'price', 'description', 'quantity', 'isActive'];
  products: Product[] = [];
  totalPages = 0;
  page = 0;
  size = 10;

  sortBy = 'name';
  sortDirection = 'asc';

  constructor(private productService: ProductService) {
    this.loadProducts();
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
