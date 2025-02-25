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
    nombre: '',
    codigo: ''
  };
  isEditing = false;
  nombreError: string = '';
  codigoError: string = '';

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

  validarCodigo(codigo: string): { isValid: boolean, message: string } {
    if (!codigo) {
      return { isValid: false, message: 'El código es obligatorio' };
    }
    
    codigo = codigo.trim();
    
    if (codigo.length > 4) {
      return { isValid: false, message: 'El código no puede tener más de 4 caracteres' };
    }

    return { isValid: true, message: '' };
  }

  onCodigoChange() {
    if (this.especialidad.codigo) {
      const validacion = this.validarCodigo(this.especialidad.codigo);
      this.codigoError = validacion.message;
    } else {
      this.codigoError = 'El código es obligatorio';
    }
  }

  onNombreChange() {
    if (!this.especialidad.nombre || this.especialidad.nombre.trim() === '') {
      this.nombreError = 'El nombre es obligatorio';
    } else {
      this.nombreError = '';
    }
  }

  onSubmit() {
    this.onNombreChange();
    this.onCodigoChange();

    if (this.nombreError || this.codigoError) {
      return;
    }

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