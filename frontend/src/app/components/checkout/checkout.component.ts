import { Component, inject } from '@angular/core';
import { CartService } from '../../shared/services/cart.service';
import { OrderService } from '../../shared/services/order.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatCard } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';


@Component({
  selector: 'app-checkout',
  imports: [
    CommonModule,
    MatCard,
    MatIcon,
    MatButtonModule
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent {
  cartService = inject(CartService);
  orderService = inject(OrderService);
  snackBar = inject(MatSnackBar);
  router = inject(Router);

  items = this.cartService.getItems();

  onSubmit() {
    const dto = this.items.map(i => ({
      productUuid: i.productUuid,
      quantity: i.quantity
    }));

    this.orderService.createOrder(dto).subscribe({
      next: () => {
        this.snackBar.open("Order placed successfully", 'Close', {duration: 5000});
        this.cartService.clearCart();
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.snackBar.open("Order failed" + err.message, "CLose", {duration: 5000});
      }
    });
  }

  get totalAmount(): number {
    return this.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  }

  removeItem(productUuid: string) {
  this.cartService.removeItem(productUuid);
  this.items = this.cartService.getItems();
}
}
