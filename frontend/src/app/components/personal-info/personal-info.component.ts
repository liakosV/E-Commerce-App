import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, FormGroupName, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { UserService } from '../../shared/services/user.service';
import { UserMoreInfo } from '../../shared/interfaces/user-more-info';
import { Region } from '../../shared/interfaces/region';

import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RegionService } from '../../shared/services/region.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-personal-info',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './personal-info.component.html',
  styleUrl: './personal-info.component.css'
})
export class PersonalInfoComponent implements OnInit {
  fb = inject(FormBuilder);
  auth = inject(AuthService);
  userService = inject(UserService);
  regionService = inject(RegionService)
  snackbar = inject(MatSnackBar);
  route = inject(ActivatedRoute);

  regions: Region[] = [];

  form = this.fb.group({

    gender: [''],
    regionId: [null as number | null],
    address: [''],
    addressNumber: [''],
    phoneNumber: ['', [Validators.minLength(10)]],
  });
  
  ngOnInit(): void {
    this.loadRegions();
    this.loadPersonalInfo(); 
  }

  loadPersonalInfo() {
    const routeUuid = this.route.snapshot.paramMap.get('uuid');
    const userId = routeUuid ||  this.auth.getUserId();
    if (userId) {
      this.userService.getUserByUuid(userId).subscribe(user => {
        if (user.userMoreInfo) {
          this.form.patchValue(user.userMoreInfo);
        }
      });
    }
  }

  loadRegions() {
    this.regionService.getRegions().subscribe({
      next: (regions) => {
        this.regions = regions;
      },
      error: (err) => {
        console.error('Failed to load regions', err);
      }
    });
  }

  onSubmit() {
    const routeUuid = this.route.snapshot.paramMap.get('uuid');
    const userId = routeUuid ||  this.auth.getUserId();
    if (!userId) return;

    if (!this.form.value.phoneNumber) {
      this.form.value.phoneNumber = null;
    }

    const payload: UserMoreInfo = this.form.value;
    this.userService.updateUserMoreInfo(userId, payload).subscribe({
      next: () => {
        this.snackbar.open("Peronal infos updated successfully", "Close", {duration: 3000});
      },
      error: (err) => {
        const errorObj = err?.error;

        if (errorObj && typeof errorObj === 'object') {
          const messages = Object.values(errorObj).join('\n');
          this.snackbar.open(messages, "Close", {duration: 5000});
          console.error(messages);
          
        }
      }
    });
  }
}
