import { Routes } from '@angular/router';
import { LoginComponent } from './componentes/login/login.component';
import { ListaCompetidoresComponent } from './componentes/lista-competidores/lista-competidores.component';
import { GestionarExpertosComponent } from './componentes/gestionar-expertos/gestionar-expertos.component';
import { CrearExpertoComponent } from './componentes/crear-experto/crear-experto.component';
import { GestionarEspecialidadesComponent } from './componentes/gestionar-especialidades/gestionar-especialidades.component';
import { CrearEspecialidadComponent } from './componentes/crear-especialidad/crear-especialidad.component';
import { VisualizarGanadoresComponent } from './componentes/visualizar-ganadores/visualizar-ganadores.component';
import { VerExpertoComponent } from './componentes/ver-experto/ver-experto.component';
import { VerEspecialidadComponent } from './componentes/ver-especialidad/ver-especialidad.component';
import { GestionarParticipantesComponent } from './componentes/gestionar-participantes/gestionar-participantes.component';
import { CrearParticipantesComponent } from './componentes/crear-participantes/crear-participantes.component';
import { VerParticipantesComponent } from './componentes/ver-participantes/ver-participantes.component';
import { GestionarPuntuacionesComponent } from './componentes/gestionar-puntuaciones/gestionar-puntuaciones.component';

export const routes: Routes = [
  // Rutas públicas
  { path: '', component: ListaCompetidoresComponent },
  { path: 'login', component: LoginComponent },

  // Rutas de administrador
  { path: 'admin', children: [
    { path: 'especialidades', component: GestionarEspecialidadesComponent },
    { path: 'crear-especialidad', component: CrearEspecialidadComponent },
    { path: 'editar-especialidad/:id', component: CrearEspecialidadComponent },
    { path: 'expertos', component: GestionarExpertosComponent },
    { path: 'crear-experto', component: CrearExpertoComponent },
    { path: 'ver-experto/:id', component: VerExpertoComponent },
    { path: 'editar-experto/:id', component: CrearExpertoComponent },
    { path: 'ganadores', component: VisualizarGanadoresComponent },
    { path: 'ver-especialidad/:id', component: VerEspecialidadComponent },
    { path: 'participantes', component: GestionarParticipantesComponent },
    { path: 'crear-participante', component: CrearParticipantesComponent },
    { path: 'ver-participante/:id', component: VerParticipantesComponent },
    { path: 'editar-participante/:id', component: CrearParticipantesComponent }
  ]},

  // Rutas de experto
  { path: 'experto', children: [
    { path: 'participantes', component: GestionarParticipantesComponent },
    { path: 'puntuaciones', component: GestionarPuntuacionesComponent }
  ]},

  { path: '**', redirectTo: '' }
];