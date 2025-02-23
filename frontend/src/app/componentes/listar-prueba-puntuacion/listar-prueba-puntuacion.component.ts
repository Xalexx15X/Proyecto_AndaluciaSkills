import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PruebasService } from '../../servicios/pruebas.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listar-pruebas-puntuacion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listar-prueba-puntuacion.component.html',
  styleUrls: ['./listar-prueba-puntuacion.component.css']
})
export class ListarPruebaPuntuacionComponent implements OnInit {
  pruebas: any[] = [];
  participanteId: number | null = null;
  loading = false;
  error: string | null = null;
  especialidadId: number | null = null;

  constructor(
    private pruebasService: PruebasService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    // Obtener el ID del participante de la URL
    this.route.params.subscribe(params => {
      this.participanteId = +params['participanteId']; // El + convierte el string a número
      this.cargarPruebas();
    });
  }

  cargarPruebas() {
    this.loading = true;
    this.error = null;

    this.pruebasService.getPruebasByEspecialidad().subscribe({
      next: (data) => {
        console.log('Pruebas cargadas:', data);
        this.pruebas = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar las pruebas:', error);
        this.error = 'Error al cargar las pruebas de la especialidad';
        this.loading = false;
      }
    });
  }

  seleccionarPrueba(pruebaId: number) {
    this.router.navigate(['/experto/puntuar-items', this.participanteId, pruebaId]);
  }
}
