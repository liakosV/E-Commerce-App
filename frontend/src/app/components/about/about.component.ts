import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  imports: [],
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent {
  currentYear: number = new Date().getFullYear();
  authorName: string = "Βασίλειος Λιάκος | Vasileios Liakos"
}
