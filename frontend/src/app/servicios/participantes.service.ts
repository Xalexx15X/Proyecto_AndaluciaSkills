import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParticipantesService {
  private apiUrl = 'http://localhost:9000/api/participantes';

  constructor(private http: HttpClient) {}

  getParticipantes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`, {
      headers: this.getAuthHeaders()
    });
  }

  getParticipante(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/BuscarParticipante/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  crearParticipante(participante: any): Observable<any> {
    // Asegurarse de que el ID de especialidad sea un número
    if (participante.especialidad_id_especialidad) {
      participante.especialidad_id_especialidad = Number(participante.especialidad_id_especialidad);
    }
    
    const headers = this.getAuthHeaders();
    console.log('Datos a enviar:', participante); // Para depuración
    return this.http.post<any>(`${this.apiUrl}/CrearParticipante`, participante, { headers });
  }

  editarParticipante(id: number, participante: any): Observable<any> {
    if (participante.especialidad_id_especialidad) {
      participante.especialidad_id_especialidad = Number(participante.especialidad_id_especialidad);
    }

    const headers = this.getAuthHeaders();
    console.log('Datos a enviar:', participante); // Para depuración
    return this.http.put<any>(`${this.apiUrl}/ModificarParticipante/${id}`, participante, { headers });
  }

  borrarParticipante(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/BorrarParticipante/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}
