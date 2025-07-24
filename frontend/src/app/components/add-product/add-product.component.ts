import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ProductService } from '../../shared/services/product.service';
import { Category } from '../../shared/interfaces/category';
import { UserService } from '../../shared/services/user.service';
import { CategoryService } from '../../shared/services/category.service';
import { ProductInsertDto } from '../../shared/interfaces/product';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BackButtonComponent } from "../tools/back-button/back-button.component";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    BackButtonComponent
],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  fb = inject(FormBuilder);
  router = inject(Router);
  productService = inject(ProductService);
  userService = inject(UserService);
  categoryService = inject(CategoryService);
  snackBar = inject(MatSnackBar)

  categories: Category[] = [];

  form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0.01)]],
    quantity: [0, [Validators.required, Validators.min(0)]],
    categoryId: [null, Validators.required],
  });

  constructor() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (res) => (this.categories = res),
      error: (err) => console.error('Failed to load categories', err),
    });
  }

onSubmit() {
  if (this.form.invalid) return;

  const formValue = this.form.value;

  const payload: ProductInsertDto = {
    name: formValue.name!,
    description: formValue.description || '',
    price: formValue.price!,
    quantity: formValue.quantity!,
    categoryId: formValue.categoryId!,
  };

  this.productService.createProduct(payload).subscribe({
    next: (res) => {
      console.log('Product created:', res);
      this.form.reset();
      this.router.navigate(['']);
    },
    error: (err) => {
      const errorObj = err?.error;
      
      if (errorObj && typeof errorObj === 'object') {
        const messages = Object.values(errorObj).join(',\n');
        this.snackBar.open(messages, 'Close', {
          duration: 10000
        })
      } else {
        alert('Creation failed. Please try again.');
      }
    }
  });
}
}
