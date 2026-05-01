//import { Routes } from '@angular/router';

//export const routes: Routes = [];



import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard';
import { OrdresComponent } from './components/ordres/ordres';
import { ProduitsComponent } from './components/produits/produits';
import { MachinesComponent } from './components/machines/machines';
import { EmployesComponent } from './components/employes/employes';
 
export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'ordres', component: OrdresComponent },
  { path: 'produits', component: ProduitsComponent },
  { path: 'machines', component: MachinesComponent },
  { path: 'employes', component: EmployesComponent }
];
