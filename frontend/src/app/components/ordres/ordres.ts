//import { Component } from '@angular/core';

//@Component({
  //selector: 'app-ordres',
  //imports: [],
  //templateUrl: './ordres.html',
  //styleUrl: './ordres.css',
//})
//export class Ordres {}


import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, OrdreFabrication, Produit, Machine, Employe } from '../../services/api';

@Component({
  selector: 'app-ordres',
  imports: [CommonModule, FormsModule],
  templateUrl: './ordres.html',
  styleUrl: './ordres.css'
})
export class OrdresComponent implements OnInit {

  ordres: OrdreFabrication[] = [];
  produits: Produit[] = [];
  machines: Machine[] = [];
  employes: Employe[] = [];

  showModal = false;
  isEditing = false;
  loading = false;

  selectedProduitId: number | null = null;
  selectedMachineId: number | null = null;
  selectedEmployeId: number | null = null;

  form: OrdreFabrication = {
    projet: '',
    quantite: 1,
    date: new Date().toISOString().split('T')[0],
    etat: 'EN_ATTENTE',
    produit: { id: 0 }
  };

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
    this.api.getProduits().subscribe(d => this.produits = d);
    this.api.getMachines().subscribe(d => this.machines = d);
    this.api.getEmployes().subscribe(d => this.employes = d);
  }

  load() {
    this.api.getOrdres().subscribe(data => this.ordres = data);
  }

  openCreate() {
    this.isEditing = false;
    this.form = {
      projet: '',
      quantite: 1,
      date: new Date().toISOString().split('T')[0],
      etat: 'EN_ATTENTE',
      produit: { id: 0 }
    };
    this.selectedProduitId = null;
    this.selectedMachineId = null;
    this.selectedEmployeId = null;
    this.showModal = true;
  }

  openEdit(o: OrdreFabrication) {
    this.isEditing = true;
    this.form = { ...o };
    this.selectedProduitId = (o.produit as any)?.id ?? null;
    this.selectedMachineId = (o.machine as any)?.id ?? null;
    this.selectedEmployeId = (o.employe as any)?.id ?? null;
    this.showModal = true;
  }

  save() {
    if (!this.selectedProduitId) return;
    this.loading = true;

    const payload: OrdreFabrication = {
      ...this.form,
      produit: { id: this.selectedProduitId },
      machine: this.selectedMachineId ? { id: this.selectedMachineId } : undefined,
      employe: this.selectedEmployeId ? { id: this.selectedEmployeId } : undefined
    };

    if (this.isEditing && this.form.id) {
      this.api.updateOrdre(this.form.id, payload).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    } else {
      this.api.createOrdre(payload).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    }
  }

  delete(id: number) {
    if (confirm('Supprimer cet ordre ?')) {
      this.api.deleteOrdre(id).subscribe(() => this.load());
    }
  }

  changerEtat(id: number, etat: string) {
    this.api.changerEtatOrdre(id, etat).subscribe(() => this.load());
  }

  getProduitNom(o: OrdreFabrication): string {
    const p = this.produits.find(p => p.id === (o.produit as any)?.id);
    return p ? p.nom : 'Produit inconnu';
  }

  getMachineNom(o: OrdreFabrication): string {
    if (!(o.machine as any)?.id) return '—';
    const m = this.machines.find(m => m.id === (o.machine as any)?.id);
    return m ? m.nom : '—';
  }

  getEmployeNom(o: OrdreFabrication): string {
    if (!(o.employe as any)?.id) return '—';
    const e = this.employes.find(e => e.id === (o.employe as any)?.id);
    return e ? e.nom : '—';
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

  getProchainEtat(etat: string): string | null {
    switch(etat) {
      case 'EN_ATTENTE': return 'EN_COURS';
      case 'EN_COURS': return 'TERMINE';
      default: return null;
    }
  }

  getProchainLabel(etat: string): string {
    switch(etat) {
      case 'EN_ATTENTE': return 'Démarrer';
      case 'EN_COURS': return 'Terminer';
      default: return '';
    }
  }
}
