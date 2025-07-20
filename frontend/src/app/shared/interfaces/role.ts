export interface Role {
  id: number;
  name: string;
  description: string;
}

export interface RoleInsertDto {
  name: string;
  description: string;
}
