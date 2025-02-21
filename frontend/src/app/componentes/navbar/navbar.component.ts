import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(public authService: AuthService, private router: Router) { }

  getNombreCompleto(): string {
    const datos = localStorage.getItem('DATOS_AUTH');
    if (datos) {
      const datosJson = JSON.parse(datos);
      if (datosJson.usuario) {
        return `${datosJson.usuario.nombre} ${datosJson.usuario.apellidos}`;
      }
    }
    return 'Usuario';
  }

  getRol(): string {
    const datos = localStorage.getItem('DATOS_AUTH');
    if (datos) {
      const datosJson = JSON.parse(datos);
      if (datosJson.usuario && datosJson.usuario.role) {
        return datosJson.usuario.role === 'ROLE_ADMIN' ? 'Administrador' : 'Experto';
      }
    }
    return '';
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}