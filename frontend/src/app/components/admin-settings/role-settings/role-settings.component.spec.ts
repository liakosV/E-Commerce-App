import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleSettingsComponent } from './role-settings.component';

describe('RoleSettingsComponent', () => {
  let component: RoleSettingsComponent;
  let fixture: ComponentFixture<RoleSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleSettingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
