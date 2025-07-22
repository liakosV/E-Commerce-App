import { Component, inject, OnInit } from '@angular/core';
import { RegionService } from '../../../shared/services/region.service';
import { Region } from '../../../shared/interfaces/region';

import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-region-settings',
  imports: [
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './region-settings.component.html',
  styleUrl: './region-settings.component.css'
})
export class RegionSettingsComponent implements OnInit {
  regionService = inject(RegionService);
  snackbar = inject(MatSnackBar);

  regions: Region[] = [];
  regionName = '';

  ngOnInit(): void {
    this.loadRegions();
  }

  loadRegions() {
    this.regionService.getRegions().subscribe({
      next: (data) => this.regions = data,
      error: (err) => this.snackbar.open("Failed to load regions", "Close", {duration: 5000})
    });
  }

  createRegion() {
    if (!this.regionName.trim()) {
      return;
    }

    this.regionService.createRegion({name: this.regionName}).subscribe({
      next: () => {
        this.snackbar.open("Region created", "Close", {duration: 3000});
        this.regionName = '';
        this.loadRegions();
      },
      error: (err) => {
        this.snackbar.open("Failed. " + err.error.description, "Close", {duration: 5000});
        
      }
    });
  }

  removeRegion(id: number) {
    this.regionService.removeRegion(id).subscribe({
      next: () => {
        this.snackbar.open("Region removed", "Close", {duration: 3000});
        this.loadRegions();
      },
      error:(err) => {
        this.snackbar.open("Failed to remove region. " + err.error.description, "Close", {duration: 5000});
      },
    });
  }
}
