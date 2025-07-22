import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatSidenavContent, MatSidenavContainer, MatSidenav } from "@angular/material/sidenav";
import { MatListModule } from "@angular/material/list";

@Component({
  selector: 'app-admin-settings',
  imports: [
    RouterModule, 
    MatSidenavContent, 
    MatSidenavContainer, 
    MatSidenav, 
    MatListModule
  ],
  templateUrl: './admin-settings.component.html',
  styleUrl: './admin-settings.component.css'
})
export class AdminSettingsComponent {

}
