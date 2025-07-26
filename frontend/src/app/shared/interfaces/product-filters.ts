import { Category } from "./category";

export interface ProductFilters {
  category?: Category,
  minPrice?: number,
  maxPrice?: number,
  isActive?: boolean,
  search?: string
}
