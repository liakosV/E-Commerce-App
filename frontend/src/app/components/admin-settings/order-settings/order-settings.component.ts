import { Component, inject, OnInit } from '@angular/core';
import { OrderService } from '../../../shared/services/order.service';
import { Page } from '../../../shared/interfaces/page';
import { Order } from '../../../shared/interfaces/order';
import { Sort } from '@angular/material/sort';

import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-order-settings',
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './order-settings.component.html',
  styleUrl: './order-settings.component.css'
})
export class OrderSettingsComponent implements OnInit {
  orderService = inject(OrderService);
  snackbar = inject(MatSnackBar);

  ordersPage!: Page<Order>;
  sortBy = 'id';
  sortDirection = 'asc';

  get orders(): Order[] {
    return this.ordersPage?.content || [];
  }

  get totalPages(): number {
    return this.ordersPage?.totalPages || 0;
  }

  get page(): number {
    return this.ordersPage?.number || 0;
  }

  get size(): number {
    return this.ordersPage?.size || 10;
  }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService
    .getAllOrder(this.page, this.size, this.sortBy, this.sortDirection)
    .subscribe({
      next: (data) => this.ordersPage = data,
      error: (err) => this.snackbar.open("Failed to load orders", "Close", {duration: 5000})
    });
  }

  deactivateOrder(order: Order) {
    const status = !order.isActive;
    this.orderService.deactivateOrder(order.uuid).subscribe({
      next: () => {
        this.snackbar.open("Order status changed to " + status, "Close", {duration: 3000});
        this.loadOrders();
      },
      error: (err) => {
        this.snackbar.open("Failed to change the status of the order.", "Close", {duration: 5000});
      }
    })
  }

  removeOrder(order: Order) {
    this.orderService.removeOrder(order.uuid).subscribe({
        next: () => {
        this.snackbar.open("Order removed ", "Close", {duration: 3000});
        this.loadOrders();
      },
      error: (err) => {
        this.snackbar.open("Failed to remove order. Reson: " + err.error.description, "Close", {duration: 5000});
      }
    })
  }

    changePage(newPage: number) {
      if (newPage >= 0 && newPage < this.totalPages) {
        this.ordersPage.number = newPage;
        this.loadOrders();
      }
    }
  
    onSortChange(sort: Sort) {
      if (!sort.direction) return;
      this.sortBy = sort.active;
      this.sortDirection = sort.direction || "asc";
      this.loadOrders();
    }
}
