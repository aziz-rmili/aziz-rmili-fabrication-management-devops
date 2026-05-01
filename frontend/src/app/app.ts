//import { Component, signal } from '@angular/core';
//import { RouterOutlet } from '@angular/router';

//@Component({
  //selector: 'app-root',
  //imports: [RouterOutlet],
  //templateUrl: './app.html',
  //styleUrl: './app.css'
//})
//export class App {
 // protected readonly title = signal('frontend');
//}




import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar';
 
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SidebarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'GestionFab';
}
