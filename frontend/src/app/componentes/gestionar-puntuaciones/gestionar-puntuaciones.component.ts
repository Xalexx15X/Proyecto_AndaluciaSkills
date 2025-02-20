import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PuntuacionesService } from '../../servicios/puntuaciones.service';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-gestionar-puntuaciones',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './gestionar-puntuaciones.component.html',
  styleUrls: ['./gestionar-puntuaciones.component.css']
})
export class GestionarPuntuacionesComponent implements OnInit {
  participantes: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private puntuacionesService: PuntuacionesService,
    private authService: AuthService
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

  puntuar(participanteId: number) {
    // Aquí implementaremos la lógica para puntuar
    console.log('Puntuando al participante:', participanteId);
  }
}
