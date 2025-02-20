import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EspecialidadService } from '../../servicios/especialidad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-crear-especialidad',
  templateUrl: './crear-especialidad.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearEspecialidadComponent implements OnInit {
  especialidad: any = {
    nombre: ''
  };
  isEditing = false;

  constructor(
    private especialidadService: EspecialidadService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditing = true;
      this.especialidadService.getEspecialidad(id).subscribe(
        data => {
          this.especialidad = data;
        }
      );
    }
  }

  onSubmit() {
    if (this.isEditing) {
      this.especialidadService.editarEspecialidad(this.especialidad.idEspecialidad, this.especialidad).subscribe(
        response => {
          console.log('Respuesta del servidor:', response);
          this.router.navigate(['admin/especialidades']);
        },
        error => console.error('Error al editar:', error)
      );
    } else {
      this.especialidadService.crearEspecialidad(this.especialidad).subscribe(
        response => {
          console.log('Respuesta del servidor:', response);
          this.router.navigate(['admin/especialidades']);
        },
        error => console.error('Error al crear:', error)
      );
    }
  }
}