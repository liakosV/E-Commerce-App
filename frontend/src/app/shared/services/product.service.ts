import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product } from '../interfaces/product';
import { Page } from '../interfaces/page';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiURL = 'http://localhost:8080/api/products';
  http: HttpClient = inject(HttpClient);
  

  constructor() { }

  getProducts(
    page: number,
    size: number,
    sort: string,
    direction: string
  ): Observable<Page<Product>> {
    const url = `${this.apiURL}?page=${page}&size=${size}&sortBy=${sort}&sortDirection=${direction}`;
    return this.http.get<Page<Product>>(url);
  }
  
}
