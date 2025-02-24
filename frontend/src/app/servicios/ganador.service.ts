import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GanadoresService {
  private apiUrl = 'http://localhost:9000/api/evaluaciones';

  constructor(private http: HttpClient) { }

  getGanadores(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/ganadores`, { headers });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}