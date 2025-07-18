import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { PersonalInfoComponent } from './components/personal-info/personal-info.component';
import { AddProductComponent } from './components/add-product/add-product.component';

import { roleGuard } from './shared/guards/role.guard';
import { authGuard } from './shared/guards/auth.guard';
import { CheckoutComponent } from './components/checkout/checkout.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'categories', component: CategoriesComponent},
  {path: 'checkout', component: CheckoutComponent, canActivate: [authGuard]},
  {path: 'account', component: PersonalInfoComponent, canActivate: [authGuard]},
  {path: 'add-product', component: AddProductComponent, canActivate: [roleGuard], data: {roles: ['ADMIN', 'SELLER']}},
  {
  path: 'categories/:id/products',
  loadComponent: () =>
    import('./components/category-products/category-products.component').then(
      (m) => m.CategoryProductsComponent
    ),
}
];
