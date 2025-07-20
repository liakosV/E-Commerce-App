import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category, CategoryInsertDto } from '../interfaces/category';
import { Product } from '../interfaces/product';
import { ProductService } from './product.service';


const API_URL = `${environment.apiURL}/api/categories`;
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  http: HttpClient = inject(HttpClient);
  productService = inject(ProductService)

  constructor() { }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(API_URL);
  }

  createCategory(category: CategoryInsertDto): Observable<Category> {
    return this.http.post<Category>(`${API_URL}`, category);
  }

  removeCategory(id: number): Observable<Category> {
    return this.http.delete<Category>(`${API_URL}/${id}`);
  }
}
