import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ParticipantesService } from '../../servicios/participantes.service';
import { EspecialidadService } from '../../servicios/especialidad.service';

@Component({
    selector: 'app-crear-participantes',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './crear-participantes.component.html',
    styleUrls: ['./crear-participantes.component.css']
})
export class CrearParticipantesComponent implements OnInit {
    participante: any = {
        nombre: '',
        apellidos: '',
        centro: '',
        especialidad_id_especialidad: null
    };
    especialidades: any[] = [];
    isEditing = false;

    constructor(
        private participantesService: ParticipantesService,
        private especialidadService: EspecialidadService,
        private router: Router,
        private route: ActivatedRoute
    ) { }

    ngOnInit() {
        this.cargarEspecialidades();
        const id = this.route.snapshot.params['id'];
        if (id) {
            this.isEditing = true;
            this.participantesService.getParticipante(id).subscribe({
                next: (data) => {
                    console.log('Datos recibidos para edición:', data);
                    this.participante = {
                        ...data,
                        especialidad_id_especialidad: data.especialidad?.idEspecialidad
                    };
                },
                error: (error) => {
                    console.error('Error al cargar participante:', error);
                    this.router.navigate(['/participantes']);
                }
            });
        }
    }

    cargarEspecialidades() {
        this.especialidadService.getEspecialidades().subscribe({
            next: (data) => {
                this.especialidades = data;
                console.log('Especialidades cargadas:', this.especialidades);
            },
            error: (error) => console.error('Error al cargar especialidades:', error)
        });
    }

    cargarParticipante(id: number) {
        this.participantesService.getParticipante(id).subscribe({
            next: (data) => {
                console.log('Datos del participante recibidos:', data);
                this.participante = data;
            },
            error: (error) => console.error('Error al cargar participante:', error)
        });
    }

    onSubmit() {
        if (!this.validarFormulario()) {
            console.error('Formulario inválido');
            return;
        }

        if (this.isEditing) {
            const id = this.route.snapshot.params['id'];
            console.log('ID de la ruta:', id);
            console.log('Datos a actualizar:', this.participante);

            this.participantesService.editarParticipante(id, this.participante).subscribe({
                next: (response) => {
                    console.log('Participante actualizado:', response);
                    this.router.navigate(['experto/participantes']);
                },
                error: (error) => {
                    console.error('Error al actualizar:', error);
                    // Aquí podrías mostrar un mensaje de error al usuario
                }
            });
        } else {
            this.participantesService.crearParticipante(this.participante).subscribe({
                next: (response) => {
                    console.log('Participante creado:', response);
                    this.router.navigate(['experto/participantes']);
                },
                error: (error) => {
                    console.error('Error al crear:', error);
                    // Aquí podrías mostrar un mensaje de error al usuario
                }
            });
        }
    }

    private validarFormulario(): boolean {
        if (!this.participante.nombre || 
            !this.participante.apellidos || 
            !this.participante.centro || 
            !this.participante.especialidad_id_especialidad) {
            console.error('Campos requeridos no completados');
            return false;
        }
        return true;
    }
}
