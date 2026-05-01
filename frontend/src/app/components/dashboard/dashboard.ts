//import { Component } from '@angular/core';

//@Component({
 // selector: 'app-dashboard',
  //imports: [],
  //templateUrl: './dashboard.html',
  //styleUrl: './dashboard.css',
//})
//export class Dashboard {}



import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService, OrdreFabrication, Machine } from '../../services/api';
 
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
 
  ordres: OrdreFabrication[] = [];
  machines: Machine[] = [];
  totalOrdres = 0;
  ordresEnCours = 0;
  ordresEnAttente = 0;
  ordresTermines = 0;
  machinesDisponibles = 0;
  totalEmployes = 0;
 
  constructor(private api: ApiService) {}
 
  ngOnInit() {
    this.api.getOrdres().subscribe(data => {
      this.ordres = data.slice(0, 5);
      this.totalOrdres = data.length;
      this.ordresEnCours = data.filter(o => o.etat === 'EN_COURS').length;
      this.ordresEnAttente = data.filter(o => o.etat === 'EN_ATTENTE').length;
      this.ordresTermines = data.filter(o => o.etat === 'TERMINE').length;
    });
 
    this.api.getMachines().subscribe(data => {
      this.machines = data;
      this.machinesDisponibles = data.filter(m => m.etat === 'DISPONIBLE').length;
    });
 
    this.api.getEmployes().subscribe(data => {
      this.totalEmployes = data.length;
    });
  }
 
  getBadgeClass(etat: string): string {
    switch(etat) {
      case 'EN_ATTENTE': return 'badge-en-attente';
      case 'EN_COURS': return 'badge-en-cours';
      case 'TERMINE': return 'badge-termine';
      case 'ANNULE': return 'badge-annule';
      default: return '';
    }
  }
}
