import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EspecialidadService {
  private apiUrl = '/api/especialidades';

  constructor(private http: HttpClient) {}

  getEspecialidades(): Observable<any[]> {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    // Remove the duplicate 'especialidades' in the path
    return this.http.get<any[]>(`${this.apiUrl}`, { headers });
  }

  getEspecialidad(id: number): Observable<any> {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.apiUrl}/BuscarEspecialidad/${id}`, { headers });
  }

  crearEspecialidad(especialidad: any): Observable<any> {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    // Fix the endpoint to match the controller
    return this.http.post<any>(`${this.apiUrl}/CrearEspecialidad`, especialidad, { headers });
  }

  editarEspecialidad(id: number, especialidad: any): Observable<any> {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<any>(`${this.apiUrl}/ModificarEspecialidad/${id}`, especialidad, { headers });
  }

  borrarEspecialidad(id: number): Observable<any> {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    // Fix the endpoint to match the controller
    return this.http.delete<any>(`${this.apiUrl}/BorrarEspecialidad/${id}`, { headers });
  }

  // Helper method to get headers with token
  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}