import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PruebasService } from '../../servicios/pruebas.service';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-puntuar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './puntuar.component.html'
})
export class PuntuarComponent implements OnInit {
  participanteId: number = 0;
  items: any[] = [];
  puntuacionForm: FormGroup;
  prueba: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private pruebasService: PruebasService,
    private authService: AuthService  // Añadido AuthService
  ) {
    this.puntuacionForm = this.fb.group({
      valoraciones: this.fb.array([])
    });
  }

  ngOnInit() {
    this.participanteId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarItems();
  }

  get valoraciones() {
    return this.puntuacionForm.get('valoraciones') as FormArray;
  }

  cargarItems() {
    // Primero obtenemos la prueba correspondiente a la especialidad
    const especialidadId = this.authService.getEspecialidadFromToken();
    if (especialidadId === null) {
      console.error('No se encontró ID de especialidad');
      return;
    }
    this.pruebasService.getPruebaByEspecialidad(especialidadId).subscribe({
      next: (prueba) => {
        this.prueba = prueba;
        // Luego cargamos los items de esa prueba
        this.pruebasService.getItemsByPrueba(prueba.idPrueba).subscribe({
          next: (items) => {
            this.items = items;
            this.initializeForm();
          }
        });
      }
    });
  }

  initializeForm() {
    this.items.forEach(item => {
      const valoracionControl = this.fb.control('', [
        Validators.required,
        Validators.min(0),
        Validators.max(item.gradosConsecucion)
      ]);
      this.valoraciones.push(valoracionControl);
    });
  }

  onSubmit() {
    if (this.puntuacionForm.valid) {
      const valoraciones = this.valoraciones.value.map((valor: number, index: number) => ({
        itemId: this.items[index].idItem,
        valoracion: valor
      }));

      this.pruebasService.updateValoraciones(this.prueba.idPrueba, valoraciones).subscribe({
        next: () => {
          this.router.navigate(['/experto/puntuaciones']);
        }
      });
    }
  }
}
