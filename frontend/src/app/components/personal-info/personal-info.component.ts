import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, FormGroupName, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { UserService } from '../../shared/services/user.service';
import { Region, UserMoreInfo } from '../../shared/interfaces/user-more-info';

import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

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
    const userId = this.auth.getUserId(); // assumes UUID from JWT
    if (userId) {
      this.userService.getUserByUuid(userId).subscribe(user => {
        if (user.userMoreInfo) {
          this.form.patchValue(user.userMoreInfo);
        }
      });
    }
  }

  loadRegions() {
    this.userService.getRegions().subscribe({
      next: (regions) => {
        this.regions = regions;
      },
      error: (err) => {
        console.error('Failed to load regions', err);
      }
    });
  }

  onSubmit() {
    const userId = this.auth.getUserId();
    if (!userId) return;

    const payload: UserMoreInfo = this.form.value;
    this.userService.updateUserMoreInfo(userId, payload).subscribe(() => {
      // success handling
    });
  }
}
