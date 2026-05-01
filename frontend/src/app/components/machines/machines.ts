//import { Component } from '@angular/core';

//@Component({
  //selector: 'app-machines',
 // imports: [],
 // templateUrl: './machines.html',
 // styleUrl: './machines.css',
//})
//export class Machines {}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Machine } from '../../services/api';
 
@Component({
  selector: 'app-machines',
  imports: [CommonModule, FormsModule],
  templateUrl: './machines.html',
  styleUrl: './machines.css'
})
export class MachinesComponent implements OnInit {
 
  machines: Machine[] = [];
  showModal = false;
  isEditing = false;
  loading = false;
 
  form: Machine = { nom: '', etat: 'DISPONIBLE' };
 
  constructor(private api: ApiService) {}
 
  ngOnInit() { this.load(); }
 
  load() {
    this.api.getMachines().subscribe(data => this.machines = data);
  }
 
  openCreate() {
    this.isEditing = false;
    this.form = { nom: '', etat: 'DISPONIBLE' };
    this.showModal = true;
  }
 
  openEdit(m: Machine) {
    this.isEditing = true;
    this.form = { ...m };
    this.showModal = true;
  }
 
  save() {
    this.loading = true;
    if (this.isEditing && this.form.id) {
      this.api.updateMachine(this.form.id, this.form).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    } else {
      this.api.createMachine(this.form).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    }
  }
 
  delete(id: number) {
    if (confirm('Supprimer cette machine ?')) {
      this.api.deleteMachine(id).subscribe(() => this.load());
    }
  }
 
  demarrerMaintenance(id: number) {
    if (confirm('Démarrer la maintenance de cette machine ?')) {
      this.api.demarrerMaintenance(id).subscribe(() => this.load());
    }
  }
 
  terminerMaintenance(id: number) {
    this.api.terminerMaintenance(id).subscribe(() => this.load());
  }
 
  getBadgeClass(etat: string): string {
    switch(etat) {
      case 'DISPONIBLE': return 'badge-disponible';
      case 'EN_MAINTENANCE': return 'badge-maintenance';
      case 'HORS_SERVICE': return 'badge-hors-service';
      default: return '';
    }
  }
}
