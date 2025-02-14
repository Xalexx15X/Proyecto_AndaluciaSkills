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
    if (!this.username || !this.password) {
      this.error = 'Por favor, complete todos los campos';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.iniciarSesion(this.username, this.password).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Error en login:', error);
        this.error = 'Error al iniciar sesión. Por favor, verifique sus credenciales.';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}