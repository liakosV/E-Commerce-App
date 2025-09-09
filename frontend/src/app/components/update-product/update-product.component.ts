import { Component, inject, OnInit } from '@angular/core';
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
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BackButtonComponent } from "../tools/back-button/back-button.component";
import { AuthService } from '../../shared/services/auth.service';
import { RegionService } from '../../shared/services/region.service';

@Component({
  selector: 'app-update-product',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    BackButtonComponent
],
  templateUrl: './update-product.component.html',
  styleUrl: './update-product.component.css'
})
export class UpdateProductComponent implements OnInit {
  fb = inject(FormBuilder);
  auth = inject(AuthService);
  userService = inject(UserService);
  regionService = inject(RegionService);
  productService = inject(ProductService);
  categoryService = inject(CategoryService);
  snackBar = inject(MatSnackBar);
  route = inject(ActivatedRoute);
  router = inject(Router);

   form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0.01)]],
    quantity: [0, [Validators.required, Validators.min(1)]],
    categoryId: [0 , Validators.required],
  });

  categories: Category[] = [];
  productUuid!: string;

  ngOnInit(): void {
    // 1. Get product uuid from route
    this.productUuid = this.route.snapshot.paramMap.get('uuid')!;

    // 2. Load product
    this.productService.getProductByUuid(this.productUuid).subscribe({
      next: (product) => {
        this.form.patchValue({
          name: product.name,
          description: product.description,
          price: product.price,
          quantity: product.quantity,
          categoryId: product.category.id
        });
      },
      error: () => {
        this.snackBar.open('Failed to load product', 'Close', { duration: 3000 });
        this.router.navigate(['']);
      }
    });

    // 3. Load categories
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => (this.categories = categories),
      error: () => this.snackBar.open('Failed to load categories', 'Close', { duration: 3000 })
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const dto: ProductInsertDto = this.form.value as ProductInsertDto;

    this.productService.updateProduct(this.productUuid, dto).subscribe({
      next: () => {
        this.snackBar.open('Product updated successfully', 'Close', { duration: 3000 });
        this.router.navigate(['']);
      },
      error: () => this.snackBar.open('Failed to update product', 'Close', { duration: 3000 })
    });
  }

}
