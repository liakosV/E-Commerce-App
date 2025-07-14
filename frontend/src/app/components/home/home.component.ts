import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../../shared/interfaces/product';
import { ProductService } from '../../shared/services/product.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  totalPages = 0;
  page = 0;
  size = 10;

  sortBy = 'name';
  sortDirection = 'asc';

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
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
    this.page = newPage;
    this.loadProducts();
  }

  changeSort(field: string) {
    if (this.sortBy === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = field;
      this.sortDirection = 'asc';
    }
    this.loadProducts();
    console.log('sorting changed')
  }
}
