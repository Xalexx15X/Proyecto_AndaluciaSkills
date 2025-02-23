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
  providers: [PuntuacionesService, AuthService],
  templateUrl: './gestionar-puntuaciones.component.html',
  styleUrls: ['./gestionar-puntuaciones.component.css']
})
export class GestionarPuntuacionesComponent implements OnInit {
  participantes: any[] = [];
  loading = false;
  error: string | null = null;
  especialidadId: number | null = null;

  constructor(
    private puntuacionesService: PuntuacionesService,
    private authService: AuthService,
    private router: Router
  ) {
    this.especialidadId = this.authService.getEspecialidadFromToken();
  }

  ngOnInit() {
    if (!this.especialidadId) {
      this.error = 'No tiene una especialidad asignada';
      return;
    }
    this.cargarParticipantes();
  }

  cargarParticipantes() {
    this.loading = true;
    this.error = null;

    this.puntuacionesService.getParticipantesByEspecialidad().subscribe({
      next: (data) => {
        console.log('Participantes de la especialidad cargados:', data);
        this.participantes = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar participantes:', error);
        this.error = 'Error al cargar los participantes de la especialidad';
        this.loading = false;
      }
    });
  }

  puntuar(participanteId: number) {
    // Cambiar la ruta para incluir el prefijo 'experto'
    this.router.navigate(['/experto/listar-prueba-puntuacion', participanteId]);
  }
}
