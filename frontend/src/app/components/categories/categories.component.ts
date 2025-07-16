import { Component, inject, OnInit } from '@angular/core';
import { CategoryService } from '../../shared/services/category.service';
import { Category } from '../../shared/interfaces/category';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories',
  imports: [CommonModule, RouterModule, MatCardModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  categoryService = inject(CategoryService);
  router = inject(Router);

  ngOnInit(): void {
    this.categoryService.getAllCategories().subscribe((data) => {
      this.categories = data;
    })
  }

  goToCategory(id: number) {
    this.router.navigate(['/categories', id, 'products']);
  }

}
