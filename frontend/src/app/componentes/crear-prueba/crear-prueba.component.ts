// crear-prueba.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PruebasService } from '../../servicios/pruebas.service';

@Component({
  selector: 'app-crear-prueba',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './crear-prueba.component.html',
  styleUrls: ['./crear-prueba.component.css']
})
export class CrearPruebaComponent implements OnInit {
  pruebaForm: FormGroup;
  especialidades: any[] = [];
  selectedFile: File | null = null;
  isFormValid: boolean = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private pruebasService: PruebasService
  ) {
    this.pruebaForm = this.fb.group({
      enunciado: ['', [Validators.required]], // Aquí guardaremos la URL del PDF
      puntuacionMaxima: ['', [Validators.required, Validators.min(1)]],
      especialidadId: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    this.cargarEspecialidades();
  }

  cargarEspecialidades() {
    this.pruebasService.getEspecialidades().subscribe({
      next: (data: any[]) => this.especialidades = data,
      error: (error: any) => console.error('Error:', error)
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedFile = file;
      this.checkFormValidity();
    } else {
      this.selectedFile = null;
      // Mostrar mensaje de error
      console.error('Por favor, selecciona un archivo PDF válido');
    }
  }

  checkFormValidity() {
    this.isFormValid = this.pruebaForm.valid && this.selectedFile !== null;
  }

  onSubmit() {
    if (this.isFormValid) {
      // Crear FormData para enviar el archivo
      const formData = new FormData();
      if (this.selectedFile) {
        formData.append('file', this.selectedFile);
      }
      
      // Crear objeto con los datos del formulario
      const pruebaData = {
        puntuacionMaxima: this.pruebaForm.get('puntuacionMaxima')?.value,
        especialidadId: this.pruebaForm.get('especialidadId')?.value
      };
      
      // Convertir a JSON y añadir al FormData
      formData.append('prueba', JSON.stringify(pruebaData));

      this.pruebasService.createPruebaWithFile(formData).subscribe({
        next: (response) => {
          console.log('Prueba creada con éxito:', response);
          this.router.navigate(['/admin/gestionar-items', response.idPrueba]);
        },
        error: (error) => {
          console.error('Error al crear la prueba:', error);
        }
      });
    }
  }
}