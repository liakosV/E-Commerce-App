import { UserMoreInfo } from "./user-more-info"

export interface User {
  username: string,
  firstname: string,
  lastname: string,
  password: string,
  email: string,
  roleName: string
}

export interface Credentials {
  username: string,
  password: string
}

export interface LoggedInUser {
  username: string,
  email: string,
  role: [string]
}

export interface AuthenticationResponseDto {
  firstname: string,
  lastname: string,
  token: string
}

export interface UserReadOnlyDto {
  id: string;
  username: string;
  firstname: string;
  lastname: string;
  password: string;
  email: string;
  roleName: string;
  userMoreInfo?: UserMoreInfo;
}