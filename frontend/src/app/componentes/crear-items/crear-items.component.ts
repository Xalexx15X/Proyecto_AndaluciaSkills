import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PruebasService } from '../../servicios/pruebas.service';
import { AuthService } from '../../servicios/auth.service'; // Añadir esta importación

@Component({
  selector: 'app-crear-items',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './crear-items.component.html',
  styleUrls: ['./crear-items.component.css']
})
export class CrearItemsComponent implements OnInit {
  itemsForm: FormGroup;
  pruebaId: number = 0;
  prueba: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private pruebasService: PruebasService,
    private authService: AuthService // Añadir AuthService
  ) {
    this.itemsForm = this.fb.group({
      items: this.fb.array([])
    });
  }

  ngOnInit() {
    this.pruebaId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarPrueba();
    this.agregarItem(); // Añadir al menos un item inicial
  }

  get items() {
    return this.itemsForm.get('items') as FormArray;
  }

  agregarItem() {
    const itemForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.minLength(5)]],
      peso: [4, [Validators.required, Validators.min(1), Validators.max(10)]],
      gradosConsecucion: [25, [Validators.required, Validators.min(0), Validators.max(100)]],
      prueba_id_prueba: [this.pruebaId]
    });

    this.items.push(itemForm);
  }

  eliminarItem(index: number) {
    this.items.removeAt(index);
  }

  cargarPrueba() {
    this.pruebasService.getPruebaById(this.pruebaId).subscribe({
      next: (data: any) => {
        this.prueba = data;
      },
      error: (error: any) => console.error('Error:', error)
    });
  }

  onSubmit() {
    if (this.itemsForm.valid) {
      const items = this.itemsForm.get('items')?.value;
      
      // Asumiendo que la prueba ya existe y solo necesitamos crear los items
      items.forEach((item: any) => {
        item.prueba_id_prueba = this.pruebaId;
      });

      this.pruebasService.createItems(items).subscribe({
        next: (response: any) => {
          console.log('Items creados:', response);
          this.router.navigate(['/admin/pruebas']);
        },
        error: (error: any) => {
          console.error('Error:', error);
          // Mostrar mensaje de error al usuario
        }
      });
    }
  }
}