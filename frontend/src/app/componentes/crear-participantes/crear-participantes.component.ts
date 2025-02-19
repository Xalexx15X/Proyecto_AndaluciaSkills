import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ParticipantesService } from '../../servicios/participantes.service';
import { EspecialidadService } from '../../servicios/especialidad.service';

@Component({
  selector: 'app-crear-participante',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-participantes.component.html',
  styleUrls: ['./crear-participantes.component.css']
})
export class CrearParticipanteComponent implements OnInit {
  participante: any = {
    nombre: '',
    apellidos: '',
    centro: '',
    especialidad_id_especialidad: null
  };
  especialidades: any[] = [];
  isEditing = false;

  constructor(
    private participantesService: ParticipantesService,
    private especialidadService: EspecialidadService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.cargarEspecialidades();
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditing = true;
      this.cargarParticipante(id);
    }
  }

  cargarEspecialidades(): void {
    this.especialidadService.getEspecialidades().subscribe({
      next: (data) => {
        this.especialidades = data;
      },
      error: (error) => console.error('Error al cargar especialidades:', error)
    });
  }

  cargarParticipante(id: number): void {
    this.participantesService.getParticipante(id).subscribe({
      next: (data) => {
        this.participante = data;
      },
      error: (error) => console.error('Error al cargar participante:', error)
    });
  }

  onSubmit(): void {
    if (this.isEditing) {
      this.participantesService.editarParticipante(this.participante.idParticipante, this.participante).subscribe({
        next: () => this.router.navigate(['/participantes']),
        error: (error) => console.error('Error al editar:', error)
      });
    } else {
      this.participantesService.crearParticipante(this.participante).subscribe({
        next: () => this.router.navigate(['/participantes']),
        error: (error) => console.error('Error al crear:', error)
      });
    }
  }
}
