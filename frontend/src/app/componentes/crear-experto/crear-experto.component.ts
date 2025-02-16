import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExpertoService } from '../../servicios/experto.service';
import { EspecialidadService } from '../../servicios/especialidad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-crear-experto',
  templateUrl: './crear-experto.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearExpertoComponent implements OnInit {
  experto: any = {
    username: '',
    password: '',
    nombre: '',
    apellidos: '',
    dni: '',
    role: 'ROLE_EXPERTO',
    especialidad_idEspecialidad: null
  };
  especialidades: any[] = [];
  isEditing = false;

  constructor(
    private expertoService: ExpertoService,
    private especialidadService: EspecialidadService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.cargarEspecialidades();
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditing = true;
      this.expertoService.getExperto(id).subscribe(
        data => {
          this.experto = {
            ...data,
            especialidad_idEspecialidad: data.especialidad?.idEspecialidad
          };
        }
      );
    }
  }

  cargarEspecialidades() {
    this.especialidadService.getEspecialidades().subscribe(
      data => {
        this.especialidades = data;
      },
      error => console.error('Error al cargar especialidades:', error)
    );
  }

  onSubmit() {
    // Asegurarse de que el ID de especialidad sea un número
    if (this.experto.especialidad_idEspecialidad) {
      this.experto.especialidad_idEspecialidad = Number(this.experto.especialidad_idEspecialidad);
    }

    console.log('Datos a enviar:', this.experto); // Para depuración

    if (this.isEditing) {
      this.expertoService.editarExperto(this.experto.idUser, this.experto).subscribe(
        response => {
          console.log('Respuesta del servidor:', response);
          this.router.navigate(['/expertos']);
        },
        error => console.error('Error al editar:', error)
      );
    } else {
      this.expertoService.crearExperto(this.experto).subscribe(
        response => {
          console.log('Respuesta del servidor:', response);
          this.router.navigate(['/expertos']);
        },
        error => console.error('Error al crear:', error)
      );
    }
  }
}