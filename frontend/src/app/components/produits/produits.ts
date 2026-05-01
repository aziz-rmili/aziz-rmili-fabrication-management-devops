//import { Component } from '@angular/core';

//@Component({
 //// selector: 'app-produits',
 // imports: [],
  //templateUrl: './produits.html',
  //styleUrl: './produits.css',
//})
/// class Produits {}


import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Produit } from '../../services/api';
 
@Component({
  selector: 'app-produits',
  imports: [CommonModule, FormsModule],
  templateUrl: './produits.html',
  styleUrl: './produits.css'
})
export class ProduitsComponent implements OnInit {
 
  produits: Produit[] = [];
  showModal = false;
  isEditing = false;
  loading = false;
 
  form: Produit = { nom: '', type: '', stock: 0, fournisseur: '' };
 
  constructor(private api: ApiService) {}
 
  ngOnInit() { this.load(); }
 
  load() {
    this.api.getProduits().subscribe(data => this.produits = data);
  }
 
  openCreate() {
    this.isEditing = false;
    this.form = { nom: '', type: '', stock: 0, fournisseur: '' };
    this.showModal = true;
  }
 
  openEdit(p: Produit) {
    this.isEditing = true;
    this.form = { ...p };
    this.showModal = true;
  }
 
  save() {
    this.loading = true;
    if (this.isEditing && this.form.id) {
      this.api.updateProduit(this.form.id, this.form).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    } else {
      this.api.createProduit(this.form).subscribe(() => {
        this.load(); this.showModal = false; this.loading = false;
      });
    }
  }
 
  delete(id: number) {
    if (confirm('Supprimer ce produit ?')) {
      this.api.deleteProduit(id).subscribe(() => this.load());
    }
  }
}