import { CartItem } from "./cart-item";
import { UserReadOnlyDto } from "./user";

export interface Order {
  uuid: string;
  orderId: number;
  username: string;
  isActive: boolean;
  totalAmount: number;
  items: CartItem
}
