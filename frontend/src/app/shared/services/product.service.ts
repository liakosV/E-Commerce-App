import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product, ProductInsertDto } from '../interfaces/product';
import { Page } from '../interfaces/page';
import { environment } from '../../../environments/environment.development';
import { ProductFilters } from '../interfaces/product-filters';


const API_URL = `${environment.apiURL}/api/products`;
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  http: HttpClient = inject(HttpClient);
  

  constructor() { }

  createProduct(product: ProductInsertDto): Observable<Product> {
    return this.http.post<Product>(`${API_URL}`, product)
  }

  removeProduct(uuid: string): Observable<Product> {
    return this.http.delete<Product>(`${API_URL}/${uuid}`);
  }

  getProductsFiltered(
    filters: ProductFilters,
    page: number,
    size: number,
    sort: string,
    direction: string
  ) : Observable<Page<Product>> {
    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString())
    .set('sort', `${sort},${direction}`);

    if (filters.category) params = params.set('category', filters.category.name);
    if (filters.minPrice !== undefined) params = params.set('minPrice', filters.minPrice.toString());
    if (filters.maxPrice !== undefined) params = params.set('maxPrice', filters.maxPrice.toString());
    if (filters.isActive !== undefined) params = params.set('isActive', filters.isActive.toString());
    if (filters.search) params = params.set('search', filters.search);

    return this.http.get<Page<Product>>(`${API_URL}`, { params });
  }
  
}
