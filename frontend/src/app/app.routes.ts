import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { PersonalInfoComponent } from './components/personal-info/personal-info.component';
import { AddProductComponent } from './components/add-product/add-product.component';

import { roleGuard } from './shared/guards/role.guard';
import { authGuard } from './shared/guards/auth.guard';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { AdminSettingsComponent } from './components/admin-settings/admin-settings.component';
import { RegionSettingsComponent } from './components/admin-settings/region-settings/region-settings.component';
import { RoleSettingsComponent } from './components/admin-settings/role-settings/role-settings.component';
import { CategorySettingsComponent } from './components/admin-settings/category-settings/category-settings.component';
import { UserSettingsComponent } from './components/admin-settings/user-settings/user-settings.component';
import { OrderSettingsComponent } from './components/admin-settings/order-settings/order-settings.component';
import { TermsComponent } from './components/terms/terms.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { UpdateProductComponent } from './components/update-product/update-product.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'about', component: AboutComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'terms', component: TermsComponent},
  {path: 'checkout', component: CheckoutComponent, canActivate: [authGuard]},
  {path: 'account', component: PersonalInfoComponent, canActivate: [authGuard]},
  {path: 'account/:uuid', component: PersonalInfoComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']}},
  {path: 'add-product', component: AddProductComponent, canActivate: [roleGuard], data: {roles: ['ADMIN', 'SELLER']}},
  {path: 'update-product/:uuid', component: UpdateProductComponent, canActivate: [roleGuard], data: {roles: ['ADMIN', 'SELLER']}},
  {
    path: 'admin', 
    component: AdminSettingsComponent,
    canActivate: [roleGuard], data: {roles: ['ADMIN']},
    children: [
      {path: 'region-settings', component: RegionSettingsComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']} },
      {path: 'role-settings', component: RoleSettingsComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']}},
      {path: 'category-settings', component: CategorySettingsComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']}},
      {path: 'user-settings', component: UserSettingsComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']}},
      {path: 'order-settings', component: OrderSettingsComponent, canActivate: [roleGuard], data: {roles: ['ADMIN']}}
    ]
  }
];
