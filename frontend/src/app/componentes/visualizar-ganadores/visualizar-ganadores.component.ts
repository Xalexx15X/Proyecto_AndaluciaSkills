import { Component, OnInit } from '@angular/core';
import { GanadoresService } from '../../servicios/ganador.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-visualizar-ganadores',
  templateUrl: './visualizar-ganadores.component.html',
  styleUrls: ['./visualizar-ganadores.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class VisualizarGanadoresComponent implements OnInit {
  ganadores: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(private ganadoresService: GanadoresService) { }

  ngOnInit(): void {
    this.cargarGanadores();
  }

  cargarGanadores(): void {
    this.loading = true;
    this.error = null;
    
    this.ganadoresService.getGanadores().subscribe({
      next: (data) => {
        console.log('Datos recibidos:', data);
        this.ganadores = data;
        this.loading = false;
        if (this.ganadores.length === 0) {
          this.error = 'No hay ganadores disponibles';
        }
      },
      error: (error) => {
        console.error('Error completo:', error);
        this.error = error.error?.mensaje || 'Error al cargar los ganadores';
        this.loading = false;
        this.ganadores = [];
      }
    });
  }
}