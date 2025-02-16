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

  constructor(private ganadoresService: GanadoresService) { }

  ngOnInit(): void {
    this.cargarGanadores();
  }

  cargarGanadores(): void {
    this.ganadoresService.getGanadores().subscribe(
      (data) => {
        this.ganadores = data;
      },
      (error: any) => {
        console.error('Error al cargar ganadores:', error);
      }
    );
  }
}