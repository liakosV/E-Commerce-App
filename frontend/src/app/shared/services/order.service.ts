import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';

const API_URL = `${environment.apiURL}/api/orders`

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  http: HttpClient = inject(HttpClient);

  constructor() { }

  createOrder(items: { productUuid: string, quantity: number}[]) {
    return this.http.post(`${API_URL}`, {items});
  }
}
