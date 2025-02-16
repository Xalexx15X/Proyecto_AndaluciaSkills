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

export const routes: Routes = [
  { path: '', component: ListaCompetidoresComponent },
  { path: 'login', component: LoginComponent },
  { path: 'especialidades', component: GestionarEspecialidadesComponent },
  { path: 'crear-especialidad', component: CrearEspecialidadComponent },
  { path: 'editar-especialidad/:id', component: CrearEspecialidadComponent },
  { path: 'expertos', component: GestionarExpertosComponent },
  { path: 'crear-experto', component: CrearExpertoComponent },
  { path: 'ver-experto/:id', component: VerExpertoComponent },
  { path: 'editar-experto/:id', component: CrearExpertoComponent },
  { path: 'ganadores', component: VisualizarGanadoresComponent },
  { path: 'ver-especialidad/:id', component: VerEspecialidadComponent },

  { path: '**', redirectTo: '' }
];