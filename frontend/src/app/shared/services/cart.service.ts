import { Injectable } from '@angular/core';
import { Product } from '../interfaces/product';
import { BehaviorSubject } from 'rxjs';
import { CartItem } from '../interfaces/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private itemsSubject = new BehaviorSubject<CartItem[]>([]);
  items$ = this.itemsSubject.asObservable();

  getItems(): CartItem[] {
    return this.itemsSubject.value;
  }

  addItem(item: CartItem) {
    const items = this.itemsSubject.value;
    const existing = items.find(i => i.productUuid === item.productUuid);

    if (existing) {
      existing.quantity += item.quantity;
    } else {
      items.push(item);
    }

    this.itemsSubject.next([...items]);
  }

  removeItem(productUuid: string) {
    const filtered = this.itemsSubject.value.filter(i => i.productUuid !== productUuid);
    this.itemsSubject.next(filtered);
  }

  clearCart() {
    this.itemsSubject.next([]);
  }
}
