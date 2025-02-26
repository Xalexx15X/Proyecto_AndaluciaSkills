import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as bcrypt from 'bcryptjs';


@Injectable({
  providedIn: 'root'
})
export class ExpertoService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  getExpertos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/BuscarExpertos`, {
      headers: this.getAuthHeaders()
    });
  }

  getExperto(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/BuscarExperto/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  crearExperto(experto: any): Observable<any> {
    // Crear una copia limpia del objeto experto con solo los campos necesarios
    const expertoToSend = {
        username: experto.username.trim(),
        password: experto.password.trim(), // Aseguramos que la contraseña está en texto plano
        nombre: experto.nombre.trim(),
        apellidos: experto.apellidos.trim(),
        dni: experto.dni.trim(),
        role: 'ROLE_EXPERTO',
        especialidad_id_especialidad: Number(experto.especialidad_id_especialidad)
    };
    
    const headers = this.getAuthHeaders();
    console.log('Datos a enviar al backend:', expertoToSend);
    
    return this.http.post<any>(`${this.apiUrl}/CrearExperto`, expertoToSend, { headers });
  }

  editarExperto(id: number, experto: any): Observable<any> {
    // Asegurarse de que el ID de especialidad sea un número
    if (experto.especialidad_idEspecialidad) {
      experto.especialidad_idEspecialidad = Number(experto.especialidad_idEspecialidad);
    }

    const headers = this.getAuthHeaders();
    console.log('Datos a enviar:', experto); // Para depuración
    return this.http.put<any>(`${this.apiUrl}/ModificarExperto/${id}`, experto, { headers });
  }

  borrarExperto(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/BorrarExperto/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}