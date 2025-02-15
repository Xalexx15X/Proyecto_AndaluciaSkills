import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompetidorService } from '../../servicios/competidor.service';

@Component({
  selector: 'app-lista-competidores',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-competidores.component.html',
  styleUrls: ['./lista-competidores.component.css']
})
export class ListaCompetidoresComponent implements OnInit {
  competidores: any[] = [];

  constructor(private competidorService: CompetidorService) { }

  ngOnInit(): void {
    this.obtenerCompetidores();
  }

  obtenerCompetidores(): void {
    this.competidorService.getCompetidores().subscribe(
      (data: any[]) => {
        this.competidores = data;
      },
      error => {
        console.error('Error al obtener la lista de competidores:', error);
      }
    );
  }
}