import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompetidorService {
  private apiUrl = 'http://localhost:9000/api/participantes';

  constructor(private http: HttpClient) { }

  getCompetidores(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}