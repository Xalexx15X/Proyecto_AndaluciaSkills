import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ParticipantesService } from '../../servicios/participantes.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-ver-participantes',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ver-participantes.component.html',
  styleUrl: './ver-participantes.component.css'
})
export class VerParticipantesComponent implements OnInit {
  participante: any = null;

  constructor(
    private participantesService: ParticipantesService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.cargarParticipante(id);
  }

  cargarParticipante(id: number): void {
    this.participantesService.getParticipante(id).subscribe({
      next: (data) => {
        this.participante = data;
      },
      error: (error) => {
        console.error('Error al cargar participante:', error);
        this.router.navigate(['/participantes']);
      }
    });
  }
}