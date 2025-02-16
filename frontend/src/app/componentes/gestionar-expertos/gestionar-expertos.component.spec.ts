import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarExpertosComponent } from './gestionar-expertos.component';

describe('GestionarExpertosComponent', () => {
  let component: GestionarExpertosComponent;
  let fixture: ComponentFixture<GestionarExpertosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionarExpertosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarExpertosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
