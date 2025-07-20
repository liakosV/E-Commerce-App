import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role, RoleInsertDto } from '../interfaces/role';
import { environment } from '../../../environments/environment.development';

const API_URL = `${environment.apiURL}/api/roles`;

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  http: HttpClient = inject(HttpClient);
  constructor() { }

  getAllRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(`${API_URL}`)
  }

  createRole(role: RoleInsertDto): Observable<Role> {
    return this.http.post<Role>(`${API_URL}`, role);
  }

  removeRole(id: number): Observable<Role> {
    return this.http.delete<Role>(`${API_URL}/${id}`);
  }
}
