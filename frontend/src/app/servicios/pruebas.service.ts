import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { from, Observable, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
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

  // Crear una nueva prueba (método antiguo)
  createPruebaSingle(pruebaData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/crear`, pruebaData, { headers });
  }

  createItems(items: any[]): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/CrearItems`, items, { headers });
  }

  // Obtener especialidades (si este método debería estar en otro servicio, muévelo)
  getEspecialidades(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>('http://localhost:9000/api/especialidades', { headers });
  }

  createPruebaWithItems(data: {prueba: any, items: any[]}): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/CrearPruebaConItems`, data, { headers })
      .pipe(
        catchError(error => {
          console.error('Error al crear prueba con items:', error);
          return throwError(() => error);
        })
      );
  }

  // 1. Primero crear la prueba
  createPrueba(prueba: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/CrearPrueba`, prueba, { headers });
  }

  // 2. Luego crear los items para esa prueba
  createItemsForPrueba(pruebaId: number, items: any[]): Observable<any> {
    const headers = this.getAuthHeaders();
    
    const formattedItems = items.map(item => ({
        descripcion: item.descripcion,
        peso: item.peso,
        grados_consecucion: item.grados_consecucion,
        prueba_id_Prueba: pruebaId // Asegúrate de que el nombre coincida exactamente
    }));

    console.log('Items formateados a enviar:', formattedItems);
    return this.http.post(`${this.apiUrl}/CrearItems`, formattedItems, { headers });
  }

  getPruebasByEspecialidad(): Observable<any[]> {
    const especialidadId = this.authService.getEspecialidadFromToken();
    if (!especialidadId) {
      console.error('No se encontró ID de especialidad en el token');
      return throwError(() => new Error('No se encontró ID de especialidad'));
    }
    
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/ListarPruebasPorEspecialidad/${especialidadId}`, { headers })
      .pipe(
        tap(pruebas => console.log('Pruebas obtenidas:', pruebas)),
        catchError(error => {
          console.error('Error al obtener pruebas:', error);
          return throwError(() => error);
        })
      );
  }

}