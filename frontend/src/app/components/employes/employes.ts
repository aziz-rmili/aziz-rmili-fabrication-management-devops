//import { Component } from '@angular/core';

//@Component({
  //selector: 'app-employes',
  //imports: [],
  //templateUrl: './employes.html',
  //styleUrl: './employes.css',
//})
//export class Employes {}



import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Employe, Machine } from '../../services/api';
 
@Component({
  selector: 'app-employes',
  imports: [CommonModule, FormsModule],
  templateUrl: './employes.html',
  styleUrl: './employes.css'
})
export class EmployesComponent implements OnInit {
 
  employes: Employe[] = [];
  machines: Machine[] = [];
  showModal = false;
  isEditing = false;
  loading = false;
  selectedMachineId: number | null = null;
 
  form: Employe = { nom: '', poste: '' };
 
  constructor(private api: ApiService) {}
 
  ngOnInit() {
    this.load();
    this.api.getMachines().subscribe(data => this.machines = data);
  }
 
  load() {
    this.api.getEmployes().subscribe(data => this.employes = data);
  }
 
  openCreate() {
    this.isEditing = false;
    this.form = { nom: '', poste: '' };
    this.selectedMachineId = null;
    this.showModal = true;
  }
 
  openEdit(e: Employe) {
    this.isEditing = true;
    this.form = { ...e };
    this.selectedMachineId = e.machineAssignee?.id ?? null;
    this.showModal = true;
  }
 
  save() {
    this.loading = true;
    const payload: Employe = {
      ...this.form,
      machineAssignee: this.selectedMachineId ? { id: this.selectedMachineId } : undefined
    };
    if (this.isEditing && this.form.id) {
      this.api.updateEmploye(this.form.id, payload).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    } else {
      this.api.createEmploye(payload).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    }
  }
 
  delete(id: number) {
    if (confirm('Supprimer cet employé ?')) {
      this.api.deleteEmploye(id).subscribe(() => this.load());
    }
  }
 
  getMachineName(e: Employe): string {
    if (!e.machineAssignee) return 'Non affecté';
    const m = this.machines.find(m => m.id === e.machineAssignee?.id);
    return m ? m.nom : 'Machine inconnue';
  }
}