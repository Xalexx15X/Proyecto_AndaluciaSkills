import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EspecialidadService } from '../../servicios/especialidad.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ver-especialidad',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container" *ngIf="especialidad">
      <h2>Detalles de la Especialidad</h2>
      <div class="card">
        <div class="card-body">
          <p class="card-text"><strong>ID:</strong> {{especialidad.idEspecialidad}}</p>
          <p class="card-text"><strong>Nombre:</strong> {{especialidad.nombre}}</p>
        </div>
      </div>
      <button class="btn btn-primary mt-3" (click)="volver()">Volver</button>
    </div>
  `
})
export class VerEspecialidadComponent implements OnInit {
  especialidad: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private especialidadService: EspecialidadService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.especialidadService.getEspecialidad(id).subscribe(
      data => {
        this.especialidad = data;
        console.log('Datos de la especialidad:', data); // Para depuración
      },
      error => {
        console.error('Error:', error);
        this.volver();
      }
    );
  }

  volver() {
    this.router.navigate(['admin/especialidades']);
  }
}