import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Region, RegionInsertDto } from '../interfaces/region';

const API_URL = `${environment.apiURL}/api/regions`

@Injectable({
  providedIn: 'root'
})
export class RegionService {
  http: HttpClient = inject(HttpClient);
  constructor() { }

    getRegions(): Observable<Region[]> {
    return this.http.get<Region[]>(`${API_URL}`);
  }

  createRegion(region: RegionInsertDto): Observable<Region> {
    return this.http.post<Region>(`${API_URL}`, region);
  }

  removeRegion(id: number): Observable<Region> { 
    return this.http.delete<Region>(`${API_URL}/${id}`);
  }
}
