import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionSettingsComponent } from './region-settings.component';

describe('RegionSettingsComponent', () => {
  let component: RegionSettingsComponent;
  let fixture: ComponentFixture<RegionSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionSettingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
