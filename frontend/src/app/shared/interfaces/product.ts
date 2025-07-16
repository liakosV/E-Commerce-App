export interface Product {
  uuid: string;
  name: string;
  description: string;
  price: number;
  quantity: number;
  isActive: boolean;
  category: {
    id: number;
    name: string;
  }
}
