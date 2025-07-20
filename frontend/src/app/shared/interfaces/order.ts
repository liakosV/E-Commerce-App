import { CartItem } from "./cart-item";

export interface Order {
  uuid: string;
  orderId: number;
  userId: number;
  isActive: boolean;
  items: CartItem
}
