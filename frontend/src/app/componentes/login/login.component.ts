import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit() {
    // Validaciones básicas
    if (!this.username || !this.password) {
        this.error = 'Por favor, complete todos los campos';
        return;
    }

    // Limpiar los datos antes de enviarlos
    const username = this.username.trim();
    const password = this.password.trim();

    console.log('Intentando login con:', {
        username: username,
        password: password
    });

    this.loading = true;
    this.error = '';

    this.authService.iniciarSesion(username, password).subscribe({
        next: (response) => {
            console.log('Login exitoso:', response);
            this.loading = false;
            this.router.navigate(['/']);
        },
        error: (error) => {
            console.error('Error en login:', error);
            this.loading = false;
            if (error.status === 401) {
                this.error = 'Usuario o contraseña incorrectos';
            } else {
                this.error = 'Error al conectar con el servidor';
            }
        }
    });
  }
}