import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ParticipantesService } from '../../servicios/participantes.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-gestionar-participantes',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './gestionar-participantes.component.html',
  styleUrl: './gestionar-participantes.component.css'
})
export class GestionarParticipantesComponent implements OnInit {
  participantes: any[] = [];

  constructor(
    private participantesService: ParticipantesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarParticipantes();
  }

  cargarParticipantes(): void {
    this.participantesService.getParticipantes().subscribe({
      next: (data) => {
        this.participantes = data;
      },
      error: (error) => {
        console.error('Error al cargar participantes:', error);
      }
    });
  }

  verParticipante(id: number): void {
    this.router.navigate(['/admin/ver-participante', id]);
  }

  editarParticipante(id: number): void {
    this.router.navigate(['/admin/editar-participante', id]);
  }

  borrarParticipante(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este participante?')) {
      this.participantesService.borrarParticipante(id).subscribe({
        next: () => {
          this.cargarParticipantes();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
        }
      });
    }
  }
}
