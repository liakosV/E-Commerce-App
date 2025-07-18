import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Region, UserMoreInfo } from '../interfaces/user-more-info';
import { environment } from '../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User, UserReadOnlyDto } from '../interfaces/user';


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

  updateUserMoreInfo(userId: string, payload: UserMoreInfo): Observable<void> {
    return this.http.put<void>(`${API_URL}/${userId}`, payload)
  }

  getRegions(): Observable<Region[]> {
    return this.http.get<Region[]>(`${environment.apiURL}/api/regions`)
  }
}
