import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarPuntuacionesComponent } from './gestionar-puntuaciones.component';

describe('GestionarPuntuacionesComponent', () => {
  let component: GestionarPuntuacionesComponent;
  let fixture: ComponentFixture<GestionarPuntuacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionarPuntuacionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarPuntuacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
