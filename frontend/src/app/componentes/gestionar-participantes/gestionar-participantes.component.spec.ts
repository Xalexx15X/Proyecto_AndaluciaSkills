import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarParticipantesComponent } from './gestionar-participantes.component';

describe('GestionarParticipantesComponent', () => {
  let component: GestionarParticipantesComponent;
  let fixture: ComponentFixture<GestionarParticipantesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionarParticipantesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarParticipantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
