import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserMoreInfo } from '../interfaces/user-more-info';
import { Region } from '../interfaces/region';
import { environment } from '../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User, UserReadOnlyDto } from '../interfaces/user';
import { Page } from '../interfaces/page';


const API_URL = `${environment.apiURL}/api/users`
@Injectable({
  providedIn: 'root'
})
export class UserService {
  http: HttpClient = inject(HttpClient);

  constructor() { }

  getUserByUuid(userId: string): Observable<UserReadOnlyDto> {
    return this.http.get<UserReadOnlyDto>(`${API_URL}/${userId}`)
  }

  getAllUsers(
    page: number,
    size: number,
    sort: string,
    direction: string
  ): Observable<Page<UserReadOnlyDto>> {
    return this.http.get<Page<UserReadOnlyDto>>(`${API_URL}?page=${page}&size=${size}&sortBy=${sort}&sortDirection=${direction}`);
  }

  deactivateUser(userId: string): Observable<UserReadOnlyDto> {
    return this.http.patch<UserReadOnlyDto>(`${API_URL}/${userId}`, {});
  }

  removeUser(UserUuid: string): Observable<UserReadOnlyDto> {
    return this.http.delete<UserReadOnlyDto>(`${API_URL}/${UserUuid}`);
  }

  updateUserMoreInfo(userId: string, payload: UserMoreInfo): Observable<void> {
    return this.http.put<void>(`${API_URL}/${userId}`, payload)
  }


}
