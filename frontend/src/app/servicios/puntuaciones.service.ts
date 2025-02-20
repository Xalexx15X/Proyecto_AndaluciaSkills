import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PuntuacionesService {
  private apiUrl = 'http://localhost:9000/api/participantes';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  getParticipantesByEspecialidad(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    const especialidadId = this.authService.getEspecialidadFromToken();

    if (especialidadId === null) {
      console.error('Especialidad ID es null');
      return throwError(() => new Error('No se encontró el ID de especialidad'));
    }

    return this.http.get<any[]>(`${this.apiUrl}/porEspecialidad/${especialidadId}`, { headers })
      .pipe(
        tap(response => console.log('Participantes obtenidos:', response)),
        catchError(error => {
          console.error('Error obteniendo participantes:', error);
          return throwError(() => error);
        })
      );
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}
