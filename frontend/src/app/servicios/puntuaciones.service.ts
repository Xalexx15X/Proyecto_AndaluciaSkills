import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, throwError, from } from "rxjs";
import { AuthService } from "./auth.service";
import { Injectable } from "@angular/core";
import { catchError, switchMap, tap, concatMap, toArray, mergeMap } from 'rxjs/operators';

export interface DtoEvaluacionItem {
    evaluacion_id_evaluacion: number;
    item_id_item: number;
    valoracion: number;
    prueba_id_prueba: number;
}

export interface DtoEvaluacion {
    idEvaluacion?: number;  // Cambiado de id_evaluacion a idEvaluacion
    prueba_idPrueba: number;
    participante_idParticipante: number;
    user_idUser: number;
    notaFinal: number;
}

export interface Evaluacion {
    prueba_id_prueba: number;
    participante_id_participante: number;
    user_id_user: number;
    evaluacionItems: EvaluacionItem[];
}

export interface EvaluacionItem {
    item_id_item: number;
    valoracion: number;
    prueba_id_prueba: number;
}

@Injectable({
  providedIn: 'root'
})
export class PuntuacionesService {
  private apiUrl = 'http://localhost:9000/api';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  getParticipantesByEspecialidad(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    const especialidadId = this.authService.getEspecialidadFromToken();
    
    if (!especialidadId) {
      return throwError(() => new Error('No se encontró ID de especialidad'));
    }
    
    return this.http.get<any[]>(
      `${this.apiUrl}/participantes/porEspecialidad/${especialidadId}`,
      { headers }
    ).pipe(
      tap(participantes => {
        console.log('Participantes recibidos:', participantes);
      }),
      catchError(error => {
        console.error('Error al obtener participantes:', error);
        return throwError(() => error);
      })
    );
  }

  getItemsByPrueba(pruebaId: number): Observable<any[]> {
    const headers = this.getAuthHeaders();
    console.log('Headers enviados:', headers.get('Authorization'));
    
    return this.http.get<any[]>(
      `${this.apiUrl}/items/porPrueba/${pruebaId}`,
      { headers }
    ).pipe(
      tap(items => {
        console.log('Items recibidos:', items);
      }),
      catchError(error => {
        console.error('Error al obtener items:', error);
        if (error.status === 403) {
          console.error('Error de autorización. Redirigiendo al login...');
          // Aquí podrías llamar a authService.cerrarSesion() si es necesario
        }
        return throwError(() => error);
      })
    );
  }

  verificarEvaluacionExistente(pruebaId: number, participanteId: number): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.apiUrl}/pruebas/evaluacion/existe/${pruebaId}/${participanteId}`,
      { headers: this.getAuthHeaders() }
    );
  }

  guardarEvaluacion(evaluacion: Evaluacion): Observable<any> {
    const headers = this.getAuthHeaders();
    
    const evaluacionData = {
        pruebaId: evaluacion.prueba_id_prueba,
        participanteId: evaluacion.participante_id_participante,
        userId: evaluacion.user_id_user,
        notaFinal: 0.0
    };

    return this.http.post<DtoEvaluacion>(
        `${this.apiUrl}/pruebas/evaluacion`,
        evaluacionData,
        { headers }
    ).pipe(
        switchMap(evaluacionCreada => {
            const items = evaluacion.evaluacionItems.map(item => ({
                evaluacion_id_evaluacion: evaluacionCreada.idEvaluacion!,
                item_id_item: item.item_id_item,
                valoracion: Number(item.valoracion),
                prueba_id_prueba: evaluacion.prueba_id_prueba
            }));

            // Primero guardamos todos los items
            return this.http.post<DtoEvaluacionItem[]>(
                `${this.apiUrl}/pruebas/evaluaciones/${evaluacionCreada.idEvaluacion}/valoraciones`,
                items,
                { headers }
            ).pipe(
                tap(response => {
                    console.log('Items guardados y nota final actualizada:', response);
                })
            );
        }),
        catchError(error => {
            if (error.status === 409) {
                return throwError(() => new Error('Este participante ya ha sido evaluado en esta prueba'));
            }
            console.error('Error detallado:', error);
            return throwError(() => new Error(`Error al guardar la evaluación: ${error.message || 'Error desconocido'}`));
        })
    );
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken(); // Usar el método del AuthService
    console.log('Token desde AuthService:', token);
    
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
}