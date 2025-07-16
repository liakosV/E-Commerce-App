import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CategoriesComponent } from './components/categories/categories.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'categories', component: CategoriesComponent},
  {
  path: 'categories/:id/products',
  loadComponent: () =>
    import('./components/category-products/category-products.component').then(
      (m) => m.CategoryProductsComponent
    ),
}
];
