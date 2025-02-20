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
    especialidad_id_especialidad: null
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
            especialidad_id_especialidad: data.especialidad?.idEspecialidad
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
    if (!this.validarFormulario()) {
        console.error('Formulario inválido');
        return;
    }

    // Asegurarse de que los datos estén limpios
    const expertoData = {
        ...this.experto,
        username: this.experto.username.trim(),
        password: this.experto.password.trim(),
        nombre: this.experto.nombre.trim(),
        apellidos: this.experto.apellidos.trim(),
        dni: this.experto.dni.trim(),
        role: 'ROLE_EXPERTO',
        especialidad_id_especialidad: Number(this.experto.especialidad_id_especialidad)
    };

    console.log('Datos del experto antes de enviar:', expertoData);

    if (this.isEditing) {
        this.expertoService.editarExperto(this.experto.idUser, expertoData).subscribe({
            next: (response) => {
                console.log('Experto editado correctamente:', response);
                this.router.navigate(['admin/expertos']);
            },
            error: (error) => {
                console.error('Error al editar experto:', error);
            }
        });
    } else {
        this.expertoService.crearExperto(expertoData).subscribe({
            next: (response) => {
                console.log('Experto creado correctamente:', response);
                this.router.navigate(['admin/expertos']);
            },
            error: (error) => {
                console.error('Error al crear experto:', error);
                // Aquí podrías añadir manejo de errores específicos
            }
        });
    }
}

  private validarFormulario(): boolean {
      if (!this.experto.username || !this.experto.password || 
          !this.experto.nombre || !this.experto.apellidos || 
          !this.experto.dni || !this.experto.especialidad_id_especialidad) {
          console.error('Todos los campos son obligatorios');
          return false;
      }
      return true;
  }
}