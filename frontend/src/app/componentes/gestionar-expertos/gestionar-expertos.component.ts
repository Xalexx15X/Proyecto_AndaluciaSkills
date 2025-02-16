import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExpertoService } from '../../servicios/experto.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-gestionar-expertos',
  templateUrl: './gestionar-expertos.component.html',
  styleUrls: ['./gestionar-expertos.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class GestionarExpertosComponent implements OnInit {
  expertos: any[] = [];

  constructor(
    private expertoService: ExpertoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.obtenerExpertos();
  }

  obtenerExpertos(): void {
    this.expertoService.getExpertos().subscribe(
      data => this.expertos = data,
      error => console.error('Error:', error)
    );
  }

  verExperto(id: number): void {
    this.router.navigate(['/ver-experto', id]);
  }

  editarExperto(id: number): void {
    this.router.navigate(['/editar-experto', id]);
  }

  borrarExperto(id: number): void {
    if (confirm('¿Estás seguro de que deseas borrar este experto?')) {
      this.expertoService.borrarExperto(id).subscribe(
        () => {
          this.obtenerExpertos();
        },
        error => console.error('Error al borrar:', error)
      );
    }
  }
}