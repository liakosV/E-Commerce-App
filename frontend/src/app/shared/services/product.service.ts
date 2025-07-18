import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product, ProductInsertDto } from '../interfaces/product';
import { Page } from '../interfaces/page';
import { environment } from '../../../environments/environment.development';


const API_URL = `${environment.apiURL}/api/products`;
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  http: HttpClient = inject(HttpClient);
  

  constructor() { }

  getProducts(
    page: number,
    size: number,
    sort: string,
    direction: string
  ): Observable<Page<Product>> {
    const url = `${API_URL}?page=${page}&size=${size}&sortBy=${sort}&sortDirection=${direction}`;
    return this.http.get<Page<Product>>(url);
  }

  getAllProductsUnpaged() :Observable<Product[]> {
    return this.http.get<Product[]>(`${API_URL}/all`)
  }

  createProduct(product: ProductInsertDto): Observable<Product> {
    return this.http.post<Product>(`${API_URL}`, product)
  }
  
}
