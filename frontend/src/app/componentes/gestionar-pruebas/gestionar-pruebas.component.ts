import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { PruebasService } from '../../servicios/pruebas.service';
import { AuthService } from '../../servicios/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-gestionar-pruebas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './gestionar-pruebas.component.html',
  styleUrls: ['./gestionar-pruebas.component.css']
})
export class GestionarPruebasComponent implements OnInit {
  pruebaForm!: FormGroup;
  loading = false;
  error = '';
  selectedFile: File | null = null;
  isValidFile = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private pruebasService: PruebasService
  ) {
    this.pruebaForm = this.fb.group({
      enunciado: ['', [Validators.required, Validators.minLength(10)]],
      puntuacionMaxima: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      items: this.fb.array([])
    });
  }

  get items() {
    return this.pruebaForm.get('items') as FormArray;
  }

  agregarItem() {
    const itemForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.minLength(5)]],
      peso: [4, [Validators.required, Validators.min(1), Validators.max(10)]],
      gradosConsecucion: [25, [Validators.required, Validators.min(0), Validators.max(100)]]
    });

    this.items.push(itemForm);
  }

  eliminarItem(index: number) {
    this.items.removeAt(index);
  }

  ngOnInit() {
    if (this.items.length === 0) {
      this.agregarItem();
    }
  }

  // Añadir getters para validación
  get formInvalid(): boolean {
    return this.pruebaForm.invalid || this.items.length === 0;
  }

  get enunciadoInvalid(): boolean {
    const control = this.pruebaForm.get('enunciado');
    return control ? (control.invalid && (control.dirty || control.touched)) : false;
  }

  get puntuacionMaximaInvalid(): boolean {
    const control = this.pruebaForm.get('puntuacionMaxima');
    return control ? (control.invalid && (control.dirty || control.touched)) : false;
  }

  onFileSelect(event: any) {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedFile = file;
      this.isValidFile = true;
    } else {
      this.selectedFile = null;
      this.isValidFile = false;
    }
  }

  onSubmit() {
    if (this.pruebaForm.valid && this.selectedFile) {
      const items = this.items.value;
      this.pruebasService.createPruebaWithItems(
        this.pruebaForm.value,
        items,
        this.selectedFile
      ).subscribe({
        next: (response) => {
          console.log('Prueba creada exitosamente:', response);
          this.pruebaForm.reset();
          this.items.clear();
          this.agregarItem();
          this.selectedFile = null;
        },
        error: (error) => {
          console.error('Error al crear la prueba:', error);
        }
      });
    } else {
      // Marcar todos los campos como touched para mostrar validaciones
      Object.keys(this.pruebaForm.controls).forEach(key => {
        const control = this.pruebaForm.get(key);
        control?.markAsTouched();
      });
      
      this.error = 'Por favor, complete todos los campos requeridos correctamente';
    }
  }
}
