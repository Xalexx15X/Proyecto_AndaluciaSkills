import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';

// Primero añadimos la interfaz para la respuesta
interface AuthResponse {
  token: string;
  username: string;
  role: string;
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
    // Asegurarnos que los datos están formateados correctamente
    const credentials = {
        username: nombreUsuario.trim(), // Eliminar espacios en blanco
        password: contraseña.trim()     // Eliminar espacios en blanco
    };
    
    console.log('Datos enviados al servidor:', {
        username: credentials.username,
        password: credentials.password
    });

    // Asegurarnos que estamos usando el Content-Type correcto
    const httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json'
        })
    };
    
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials, httpOptions)
        .pipe(
            tap((response: AuthResponse) => {
                console.log('Respuesta del servidor:', response);
                if (response && response.token) {
                    this.token = response.token;
                    this.rol = response.role;
                    this.estaLogueado = true;
                    this.usuario = {
                        username: response.username,
                        role: response.role
                    };
                    this.guardarSesion();
                } else {
                    throw new Error('No se recibió token del servidor');
                }
            }),
            catchError(error => {
                console.error('Error detallado:', {
                    status: error.status,
                    message: error.message,
                    error: error.error
                });
                this.cerrarSesion();
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

  esExperto(): boolean {
    return this.rol === 'ROLE_EXPERTO';
  }
}