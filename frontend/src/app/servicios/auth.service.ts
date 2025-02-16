// frontend/src/app/servicios/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9000/api/auth';
  token: string;
  rol: string;
  estaLogueado: boolean;
  usuario: any;

  constructor(private http: HttpClient) {
    this.token = "";
    this.rol = "";
    this.estaLogueado = false;
    this.usuario = {};
    this.recuperarSesion();
  }

  public guardarSesion() {
    const datos = {
      token: this.token,
      rol: this.rol,
      estaLogueado: this.estaLogueado,
      usuario: this.usuario
    }
    localStorage.setItem("DATOS_AUTH", JSON.stringify(datos));
  }

  recuperarSesion() {
    const datos = localStorage.getItem("DATOS_AUTH");
    if (datos) {
      const datosParsed = JSON.parse(datos);
      this.token = datosParsed.token;
      this.rol = datosParsed.rol;
      this.estaLogueado = datosParsed.estaLogueado;
      this.usuario = datosParsed.usuario;
    }
  }

  iniciarSesion(nombreUsuario: string, contraseña: string): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    
    return this.http.post(`${this.apiUrl}/login`, 
      { 
        username: nombreUsuario, 
        password: contraseña 
      },
      { headers: headers }
    ).pipe(
      tap((respuesta: any) => {
        this.usuario = { nombreUsuario: respuesta.username };
        this.rol = respuesta.role;
        this.token = respuesta.token;
        this.estaLogueado = true;
        this.guardarSesion();
      }),
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  cerrarSesion() {
    this.token = "";
    this.rol = "";
    this.estaLogueado = false;
    this.usuario = {};
    localStorage.removeItem("DATOS_AUTH");
  }

  getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  }

  esAdmin(): boolean {
    return this.rol === 'ROLE_ADMIN';
  }
}