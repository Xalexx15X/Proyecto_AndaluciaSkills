import { Component, OnInit } from '@angular/core';
import { EspecialidadService } from '../../servicios/especialidad.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-gestionar-especialidades',
  templateUrl: './gestionar-especialidades.component.html',
  styleUrls: ['./gestionar-especialidades.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class GestionarEspecialidadesComponent implements OnInit {
  especialidades: any[] = [];

  constructor(
    private especialidadService: EspecialidadService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.obtenerEspecialidades();
  }

  obtenerEspecialidades(): void {
    this.especialidadService.getEspecialidades().subscribe(
      data => this.especialidades = data,
      error => console.error('Error al obtener especialidades:', error)
    );
  }

  verEspecialidad(id: number): void {
    this.router.navigate(['/admin/ver-especialidad', id]);
  }

  editarEspecialidad(id: number): void {
    this.router.navigate(['/admin/editar-especialidad', id]);
  }

  borrarEspecialidad(id: number): void {
    if (confirm('¿Estás seguro de que deseas borrar esta especialidad?')) {
      this.especialidadService.borrarEspecialidad(id).subscribe(
        () => this.obtenerEspecialidades(),
        error => console.error('Error al borrar especialidad:', error)
      );
    }
  }
}