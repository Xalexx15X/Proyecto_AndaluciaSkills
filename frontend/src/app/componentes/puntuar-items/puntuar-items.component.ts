import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PuntuacionesService } from '../../servicios/puntuaciones.service';
import { AuthService } from '../../servicios/auth.service';

export interface EvaluacionItem {
  item_idItem: number;
  valoracion: number;
  prueba_idPrueba: number;
}

export interface Evaluacion {
  prueba_idPrueba: number;
  participante_idParticipante: number;
  user_idUser: number;
  evaluacionItems: EvaluacionItem[];
}

// Añade esta interfaz
interface Item {
  idItem: number;
  descripcion: string;
  puntuacionMaxima: number;
  peso: number; // porcentaje del item
}

@Component({
  selector: 'app-puntuar-items',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './puntuar-items.component.html',
  styleUrls: ['./puntuar-items.component.css']
})
export class PuntuarItemsComponent implements OnInit {
  items: Item[] = [];
  participanteId: number = 0;
  pruebaId: number = 0;
  userId: number = 0;
  loading = false;
  error: string | null = null;
  valoraciones: { [key: number]: number } = {};
  mensaje: { tipo: string; texto: string } | null = null;

  constructor(
    private puntuacionesService: PuntuacionesService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
        this.participanteId = +params['participanteId'];
        this.pruebaId = +params['pruebaId'];
        
        // Obtener el userId del localStorage
        const authData = JSON.parse(localStorage.getItem('DATOS_AUTH') || '{}');
        this.userId = authData.usuario?.idUser;
        
        console.log('IDs inicializados:', {
            participanteId: this.participanteId,
            pruebaId: this.pruebaId,
            userId: this.userId
        });
        
        this.cargarItems();
    });
  }

  cargarItems() {
    this.loading = true;
    this.puntuacionesService.getItemsByPrueba(this.pruebaId).subscribe({
      next: (data) => {
        this.items = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los items';
        this.loading = false;
      }
    });
  }

  guardarPuntuaciones() {
    if (!this.validarPuntuaciones()) {
        this.error = 'Por favor, revisa las puntuaciones';
        return;
    }

    // Crear los items con los nombres de columna correctos
    const evaluacionItems = Object.keys(this.valoraciones).map(idItem => ({
        item_id_item: parseInt(idItem),
        valoracion: this.valoraciones[parseInt(idItem)],
        prueba_id_prueba: this.pruebaId
    }));

    const evaluacion = {
        prueba_id_prueba: this.pruebaId,
        participante_id_participante: this.participanteId,
        user_id_user: this.userId,
        evaluacionItems: evaluacionItems
    };

    console.log('Datos a enviar:', evaluacion);

    this.loading = true;
    this.puntuacionesService.guardarEvaluacion(evaluacion)
        .subscribe({
            next: (response) => {
                this.mensaje = {
                    tipo: 'success',
                    texto: 'Puntuaciones guardadas correctamente'
                };
                this.router.navigate(['/lista-pruebas']);
            },
            error: (error) => {
                this.mensaje = {
                    tipo: 'danger',
                    texto: error.message || 'Error al guardar las puntuaciones'
                };
            },
            complete: () => {
                this.loading = false;
            }
        });
  }
  
  private mostrarMensajeExito() {
    const mensaje = document.createElement('div');
    mensaje.className = 'alert alert-success';
    mensaje.textContent = 'Puntuaciones guardadas correctamente';
    document.querySelector('.container')?.prepend(mensaje);
  }

  private validarPuntuaciones(): boolean {
    return Object.keys(this.valoraciones).every(idItem => {
      const item = this.items.find(i => i.idItem === +idItem);
      if (!item) return false;
      
      // Calcular el valor máximo basado en el porcentaje
      const valorMaximo = (item.peso / 10); // Si peso es 30%, máximo será 3
      return this.valoraciones[+idItem] >= 0 && this.valoraciones[+idItem] <= valorMaximo;
    });
  }
}
