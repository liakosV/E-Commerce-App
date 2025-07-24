import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Order } from '../interfaces/order';
import { Page } from '../interfaces/page';

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

  getAllOrder(
    page: number,
    size: number,
    sort: string,
    direction: string
  ): Observable<Page<Order>> {
    return this.http.get<Page<Order>>(`${API_URL}?page=${page}&size=${size}&sortBy=${sort}&sortDirection=${direction}`);
  }

  deactivateOrder(orderUuid: string): Observable<Order> {
    return this.http.patch<Order>(`${API_URL}/${orderUuid}`, {});
  }

  removeOrder(orderUuid: string): Observable<Order> {
    return this.http.delete<Order>(`${API_URL}/${orderUuid}`);
  }
}
