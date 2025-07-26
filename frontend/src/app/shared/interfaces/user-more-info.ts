import { Region } from "./region";

export interface UserMoreInfo {
  gender?: string | null;
  region?: Region | null;
  address?: string | null;
  addressNumber?: string | null;
  phoneNumber?: string | null;
}
