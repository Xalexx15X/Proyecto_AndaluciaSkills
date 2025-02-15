import { Routes } from '@angular/router';
import { LoginComponent } from './componentes/login/login.component';
import { ListaCompetidoresComponent } from './componentes/lista-competidores/lista-competidores.component';

export const routes: Routes = [
  { path: '', component: ListaCompetidoresComponent },
  { path: 'login', component: LoginComponent }
];