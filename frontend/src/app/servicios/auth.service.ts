import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, Subject, tap, throwError, BehaviorSubject } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

interface AuthResponse {
  token: string;
  username: string;
  role: string;
  especialidadId: number;
  nombre: string;    // Añadido
  apellidos: string; // Añadido
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9000/api/auth';
  private token: string | null = null;
  private rol: string | null = null;
  private _estaLogueado = false;
  private usuario: any = null;
  private authStateChanged = new BehaviorSubject<boolean>(false);
  private especialidadId: number | null = null;

  constructor(private http: HttpClient) {
    // Intentar recuperar datos al inicio
    this.recuperarDatosGuardados();
  }

  // Añadir getter para estaLogueado
  get estaLogueado(): boolean {
    return this._estaLogueado;
  }

  // Añadir getters para nombre y rol
  getNombreCompleto(): string {
    if (this.usuario) {
      return `${this.usuario.nombre} ${this.usuario.apellidos}`.trim();
    }
    return '';
  }

  getRol(): string {
    return this.rol || '';
  }

  private recuperarDatosGuardados() {
    const datosAuth = localStorage.getItem('DATOS_AUTH');
    if (datosAuth) {
      const datos = JSON.parse(datosAuth);
      this.token = datos.token;
      this.rol = datos.rol;
      this._estaLogueado = datos.estaLogueado;
      this.usuario = datos.usuario;
      this.especialidadId = datos.especialidadId;
      this.authStateChanged.next(true);
      console.log('Datos recuperados:', {
        token: !!this.token,
        rol: this.rol,
        estaLogueado: this._estaLogueado,
        usuario: this.usuario
      });
    }
  }

  public guardarSesion(response: AuthResponse) {
    console.log('Respuesta del servidor:', response);
    const datos = {
      token: response.token,
      rol: response.role,
      estaLogueado: true,
      usuario: {
        username: response.username,
        role: response.role,
        nombre: response.nombre,
        apellidos: response.apellidos
      },
      especialidadId: response.especialidadId
    };
    
    // Actualizar el estado interno primero
    this.token = response.token;
    this.rol = response.role;
    this._estaLogueado = true;
    this.usuario = datos.usuario;
    this.especialidadId = response.especialidadId;
    
    console.log('Guardando datos en localStorage:', datos);
    localStorage.setItem('DATOS_AUTH', JSON.stringify(datos));
    this.authStateChanged.next(true);
  }

  iniciarSesion(nombreUsuario: string, contraseña: string): Observable<any> {
    const credentials = {
      username: nombreUsuario.trim(),
      password: contraseña.trim()
    };

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response && response.token) {
            this.token = response.token;
            this.rol = response.role;
            this._estaLogueado = true;
            this.usuario = {
              username: response.username,
              role: response.role,
              nombre: response.nombre,       // Añadido
              apellidos: response.apellidos  // Añadido
            };
            this.guardarSesion(response);
            this.authStateChanged.next(true); // Notificar el cambio
          }
        }),
        catchError(error => {
          console.error('Error en inicio de sesión:', error);
          return throwError(() => new Error('Error en el inicio de sesión'));
        })
      );
  }

  cerrarSesion() {
    this.token = "";
    this.rol = "";
    this._estaLogueado = false;
    this.usuario = {};
    this.especialidadId = null;
    
    localStorage.removeItem("DATOS_AUTH");
    this.authStateChanged.next(false);
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
    return this.token || '';
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

  getUserId(): number | null {
    const datosAuth = localStorage.getItem('DATOS_AUTH');
    if (datosAuth) {
      const datos = JSON.parse(datosAuth);
      const token = datos.token;
      if (token) {
        try {
          const decodedToken: any = jwtDecode(token);
          return decodedToken.id; // Asumiendo que el ID está en el token como 'id'
        } catch (error) {
          console.error('Error decodificando el token:', error);
          return null;
        }
      }
    }
    return null;
  }

  getNombreFromToken(): string | null {
    const datosAuth = localStorage.getItem('DATOS_AUTH');
    if (datosAuth) {
      const datos = JSON.parse(datosAuth);
      return datos.usuario?.nombre || null;
    }
    return null;
  }

  getApellidosFromToken(): string | null {
    const datosAuth = localStorage.getItem('DATOS_AUTH');
    if (datosAuth) {
      const datos = JSON.parse(datosAuth);
      return datos.usuario?.apellidos || null;
    }
    return null;
  }
}