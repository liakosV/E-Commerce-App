import { Component, inject, OnInit } from '@angular/core';
import { CategoryService } from '../../../shared/services/category.service';
import { Category, CategoryInsertDto } from '../../../shared/interfaces/category';

import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-category-settings',
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './category-settings.component.html',
  styleUrl: './category-settings.component.css'
})
export class CategorySettingsComponent implements OnInit {
  categoryService = inject(CategoryService);
  snackbar = inject(MatSnackBar);

  categories: Category[] = [];
  categoryInsert: CategoryInsertDto = {
    name: ''
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => this.categories = data,
      error: () => this.snackbar.open("Failed to load categories", "Close", {duration: 5000})
    });
  }

  createCategory() {
    if (!this.categoryInsert.name.trim()) {
      return;
    }

    this.categoryService.createCategory(this.categoryInsert).subscribe({
      next: () => {
        this.snackbar.open("Category created", "Close", {duration: 3000});
        this.categoryInsert.name = '';
        this.loadCategories();
      },
      error: (err) => {
        this.snackbar.open("Failed. " + err.error.description, "Close", {duration: 5000});
      }
    });
  }

  removeCategory(id: number) {
    this.categoryService.removeCategory(id).subscribe({
      next: () => {
        this.snackbar.open("Category removed", "Close", {duration: 3000});
        this.loadCategories();
      },
      error: (err) => {
        this.snackbar.open("Failed to remove category. " + err.error.description, "Close", {duration: 5000});
      }
    })
  }
}
