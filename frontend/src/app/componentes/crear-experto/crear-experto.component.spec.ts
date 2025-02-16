import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearExpertoComponent } from './crear-experto.component';

describe('CrearExpertoComponent', () => {
  let component: CrearExpertoComponent;
  let fixture: ComponentFixture<CrearExpertoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearExpertoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearExpertoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
