export interface UserMoreInfo {
  gender?: string | null;
  regionId?: number | null;
  address?: string | null;
  addressNumber?: string | null;
  phoneNumber?: string | null;
}

export interface Region {
  id: number;
  name: string;
}
