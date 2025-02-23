import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { PruebasService } from '../../servicios/pruebas.service';
import { AuthService } from '../../servicios/auth.service';
import { Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-gestionar-pruebas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './gestionar-pruebas.component.html',
  styleUrls: ['./gestionar-pruebas.component.css']
})
export class GestionarPruebasComponent implements OnInit {
  pruebaForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private pruebasService: PruebasService,
    private router: Router
  ) {
    this.pruebaForm = this.fb.group({
      enunciado: ['', [Validators.required, Validators.minLength(10)]],
      puntuacionMaxima: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      items: this.fb.array([])
    });
  }

  ngOnInit() {
    if (this.items.length === 0) {
      this.agregarItem();
    }
  }

  get items() {
    return this.pruebaForm.get('items') as FormArray;
  }

  agregarItem() {
    const itemForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.minLength(5)]],
      peso: ['', [Validators.required, Validators.min(1), Validators.max(100)]],
      gradosConsecucion: ['', [Validators.required, Validators.min(0), Validators.max(100)]]
    });
    this.items.push(itemForm);
  }

  eliminarItem(index: number) {
    if (this.items.length > 1) {
      this.items.removeAt(index);
    } else {
      this.error = 'Debe haber al menos un item';
    }
  }

  onSubmit() {
    if (this.formInvalid) {
        this.markFormGroupTouched(this.pruebaForm);
        return;
    }

    this.loading = true;
    this.error = '';

    const pruebaData = {
        enunciado: this.pruebaForm.get('enunciado')?.value,
        puntuacionMaxima: this.pruebaForm.get('puntuacionMaxima')?.value,
        especialidad_idEspecialidad: this.authService.getEspecialidadFromToken()
    };

    console.log('1. Datos de la prueba a crear:', pruebaData);

    this.pruebasService.createPrueba(pruebaData).pipe(
        switchMap(pruebaCreada => {
            console.log('2. Prueba creada:', pruebaCreada);
            
            interface ItemForm {
                descripcion: string;
                peso: string | number;
                gradosConsecucion: string | number;
            }
            // Mapear los items asegurando que los tipos sean correctos
            const items = this.items.value.map((item: ItemForm) => ({
                descripcion: item.descripcion,
                peso: Number(item.peso),  // Convertir a número
                gradosConsecucion: Number(item.gradosConsecucion),  // Convertir a número
                prueba_idPrueba: pruebaCreada.idPrueba  // Añadir el ID de la prueba
            }));

            console.log('3. Items a crear:', items);
            return this.pruebasService.createItemsForPrueba(pruebaCreada.idPrueba, items);
        })
    ).subscribe({
        next: (response) => {
            console.log('4. Items creados:', response);
            this.loading = false;
            this.pruebaForm.reset();
            this.items.clear();
            this.agregarItem();
            this.router.navigate(['/admin/pruebas']);
        },
        error: (error) => {
            console.error('Error detallado:', error);
            this.loading = false;
            if (error.error && error.error.message) {
                this.error = error.error.message;
            } else {
                this.error = 'Error al crear los items de la prueba';
            }
        }
    });
}

  get formInvalid(): boolean {
    if (this.pruebaForm.invalid) return true;
    if (this.items.length === 0) return true;
    
    const totalPeso = this.items.controls.reduce((sum, control) => 
      sum + Number(control.get('peso')?.value || 0), 0);
    
    return totalPeso !== 100;
  }

  isItemInvalid(index: number, field: string): boolean {
    const control = this.items.at(index).get(field);
    return control ? (control.invalid && control.touched) : false;
  }

  private markFormGroupTouched(formGroup: FormGroup | FormArray) {
    Object.values(formGroup.controls).forEach(control => {
      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      } else {
        control.markAsTouched();
      }
    });
  }
}