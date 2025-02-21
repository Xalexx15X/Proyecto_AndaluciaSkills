import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PuntuacionesService } from '../../servicios/puntuaciones.service';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-gestionar-puntuaciones',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  providers: [PuntuacionesService],
  templateUrl: './gestionar-puntuaciones.component.html',
  styleUrls: ['./gestionar-puntuaciones.component.css']
})
export class GestionarPuntuacionesComponent implements OnInit {
  participantes: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private puntuacionesService: PuntuacionesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarParticipantes();
  }

  cargarParticipantes() {
    this.loading = true;
    this.puntuacionesService.getParticipantesByEspecialidad().subscribe({
      next: (data) => {
        console.log('Participantes cargados:', data);
        this.participantes = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.error = 'Error al cargar los participantes';
        this.loading = false;
      }
    });
  }

  puntuar(id: number) {
    console.log('Intentando navegar a puntuar participante:', id);
    this.router.navigate(['/experto/puntuar', id]).then(
      (success) => console.log('Navegación exitosa:', success),
      (error) => console.error('Error en navegación:', error)
    );
  }
}
