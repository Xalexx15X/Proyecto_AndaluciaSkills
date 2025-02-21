import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarPruebasComponent } from './gestionar-pruebas.component';

describe('GestionarPruebasComponent', () => {
  let component: GestionarPruebasComponent;
  let fixture: ComponentFixture<GestionarPruebasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionarPruebasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarPruebasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
