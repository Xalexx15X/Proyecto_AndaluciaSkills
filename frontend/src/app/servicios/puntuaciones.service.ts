import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class PuntuacionesService {
  private apiUrl = 'http://localhost:9000/api/evaluaciones';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getParticipantesByEspecialidad(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    const especialidadId = this.authService.getEspecialidadFromToken();
    
    if (!especialidadId) {
      console.error('No se encontró ID de especialidad');
      return new Observable();
    }

    return this.http.get<any[]>(`${this.apiUrl}/porEspecialidad/${especialidadId}`, { headers });
  }

  guardarEvaluacion(evaluacion: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/crear`, evaluacion, { headers });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}