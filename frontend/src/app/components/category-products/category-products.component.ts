import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../shared/services/product.service';
import { Product } from '../../shared/interfaces/product';

import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-category-products',
  imports: [
  CommonModule,
  MatTableModule,
  MatCardModule
  ],
  templateUrl: './category-products.component.html',
  styleUrl: './category-products.component.css'
})
export class CategoryProductsComponent implements OnInit {

  products: Product[] = [];
  categoryId!: number;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.categoryId = +this.route.snapshot.paramMap.get('id')!;

    this.productService.getAllProductsUnpaged().subscribe((products) => {
      this.products = products.filter((p) => p.category.id === this.categoryId);
    });
  }
}
