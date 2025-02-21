import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PruebasService {
  private apiUrl = 'http://localhost:9000/api/pruebas';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}').token;
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  // Obtener una prueba por ID
  getPruebaById(id: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.apiUrl}/${id}`, { headers });
  }

  // Crear una nueva prueba
  createPrueba(pruebaData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/crear`, pruebaData, { headers });
  }

  // Crear múltiples items
  createItems(items: any[]): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/items/crear`, items, { headers });
  }

  // Obtener especialidades (si este método debería estar en otro servicio, muévelo)
  getEspecialidades(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>('http://localhost:9000/api/especialidades', { headers });
  }

  createPruebaWithItems(prueba: any, items: any[], file: File): Observable<any> {
    const headers = this.getAuthHeaders();
    const formData = new FormData();
    
    formData.append('file', file);
    formData.append('prueba', JSON.stringify({
        ...prueba,
        especialidad_idEspecialidad: this.authService.getEspecialidadFromToken()
    }));
    formData.append('items', JSON.stringify(items));
  
    return this.http.post(`${this.apiUrl}/CrearPruebaConItems`, formData, { headers })
        .pipe(
            catchError(error => {
                console.error('Error al crear prueba con items:', error);
                return throwError(() => error);
            })
        );
  }

  createPruebaWithFile(formData: FormData): Observable<any> {
    const headers = this.getAuthHeaders();
    // Importante: No establecer 'Content-Type' aquí, 
    // dejarlo que lo establezca automáticamente para el FormData
    return this.http.post(`${this.apiUrl}/CrearPruebaConFile`, formData, { 
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.authService.getToken()}`
      })
    }).pipe(
      catchError(error => {
        console.error('Error al crear prueba con archivo:', error);
        return throwError(() => error);
      })
    );
  }
}