import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExpertoService } from '../../servicios/experto.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ver-experto',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container" *ngIf="experto">
      <h2>Detalles del Experto</h2>
      <div class="card">
        <div class="card-body">
          <p class="card-text"><strong>Username:</strong> {{experto.username}}</p>
          <p class="card-text"><strong>Nombre:</strong> {{experto.nombre}}</p>
          <p class="card-text"><strong>DNI:</strong> {{experto.dni}}</p>
          <p class="card-text"><strong>Especialidad:</strong> {{experto.nombreEspecialidad}}</p>
        </div>
      </div>
      <button class="btn btn-primary mt-3" (click)="volver()">Volver</button>
    </div>
  `
})
export class VerExpertoComponent implements OnInit {
  experto: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private expertoService: ExpertoService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.expertoService.getExperto(id).subscribe(
      data => {
        this.experto = data;
        console.log('Datos del experto:', data); // Para depuración
      },
      error => {
        console.error('Error:', error);
        this.volver();
      }
    );
  }

  volver() {
    this.router.navigate(['/expertos']);
  }
}