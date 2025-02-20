import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ParticipantesService } from '../../servicios/participantes.service';
import { tap, map, filter } from 'rxjs/operators';

@Component({
    selector: 'app-ver-participantes',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './ver-participantes.component.html',
    styleUrls: ['./ver-participantes.component.css']
})
export class VerParticipantesComponent implements OnInit {
    participante: any = null;
    loading: boolean = true;
    error: string | null = null;

    constructor(
        private participantesService: ParticipantesService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        console.log('Componente inicializado');
        this.route.params.pipe(
            tap(params => console.log('Params:', params)),
            map(params => +params['id']),
            filter(id => !isNaN(id))
        ).subscribe({
            next: (id) => {
                console.log('Cargando participante con ID:', id);
                this.cargarParticipante(id);
            },
            error: (error) => {
                console.error('Error en params:', error);
                this.error = 'Error al obtener el ID del participante';
                this.loading = false;
            }
        });
    }

    cargarParticipante(id: number): void {
        this.loading = true;
        this.participantesService.getParticipante(id).subscribe({
            next: (data) => {
                console.log('Datos recibidos:', data);
                this.participante = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error al cargar:', error);
                this.error = 'Error al cargar el participante';
                this.loading = false;
            }
        });
    }
}