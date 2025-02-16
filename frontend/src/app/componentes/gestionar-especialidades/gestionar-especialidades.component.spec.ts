import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarEspecialidadesComponent } from './gestionar-especialidades.component';

describe('GestionarEspecialidadesComponent', () => {
  let component: GestionarEspecialidadesComponent;
  let fixture: ComponentFixture<GestionarEspecialidadesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionarEspecialidadesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarEspecialidadesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
