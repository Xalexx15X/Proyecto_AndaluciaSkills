import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CrearParticipanteComponent } from './crear-participantes.component';
import { ParticipantesService } from '../../servicios/participantes.service';
import { CommonModule } from '@angular/common';

describe('CrearParticipantesComponent', () => {
  let component: CrearParticipanteComponent;
  let fixture: ComponentFixture<CrearParticipanteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CrearParticipanteComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule,
        ReactiveFormsModule,
        CommonModule
      ],
      providers: [
        ParticipantesService
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearParticipanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Aquí puedes agregar más pruebas específicas
  it('should initialize form', () => {
    expect(component.participante).toBeDefined();
  });
});