import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, Subject, tap, throwError } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

interface AuthResponse {
  token: string;
  username: string;
  role: string;
  especialidadId: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9000/api/auth';
  token: string;
  rol: string;
  estaLogueado: boolean;
  usuario: any;
  especialidadId: number | null;
  authStateChanged = new Subject<boolean>();

  constructor(private http: HttpClient) {
    this.token = "";
    this.rol = "";
    this.estaLogueado = false;
    this.usuario = {};
    this.especialidadId = null;
    this.recuperarSesion();
  }

  public guardarSesion(response: any) {
    const datos = {
      token: response.token,
      rol: response.role,
      estaLogueado: true,
      usuario: {
        username: response.username,
        role: response.role
      },
      especialidadId: response.especialidadId
    }
    console.log('Guardando datos en localStorage:', datos);
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
      this.especialidadId = datosParsed.especialidadId;
    }
  }

  iniciarSesion(nombreUsuario: string, contraseña: string): Observable<any> {
    const credentials = {
      username: nombreUsuario.trim(),
      password: contraseña.trim()
    };

    return this.http.post<any>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response && response.token) {
            this.token = response.token;
            this.rol = response.role;
            this.estaLogueado = true;
            this.usuario = {
              username: response.username,
              role: response.role
            };
            this.guardarSesion(response);
            this.authStateChanged.next(true); // Notificar el cambio
          }
        })
      );
  }

  cerrarSesion() {
    this.token = "";
    this.rol = "";
    this.estaLogueado = false;
    this.usuario = {};
    this.especialidadId = null;
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

  esExperto(): boolean {
    return this.rol === 'ROLE_EXPERTO';
  }

  getToken(): string {
    return this.token;
  }

  getEspecialidadFromToken(): number | null {
    const datosAuth = localStorage.getItem('DATOS_AUTH');
    if (datosAuth) {
      const datos = JSON.parse(datosAuth);
      if (datos.especialidadId) {
        console.log('Especialidad ID encontrada:', datos.especialidadId);
        return datos.especialidadId;
      }
    }
    console.log('No se encontró especialidad ID en localStorage');
    return null;
  }
}