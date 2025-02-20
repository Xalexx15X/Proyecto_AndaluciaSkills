import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CrearParticipantesComponent } from './crear-participantes.component';
import { ParticipantesService } from '../../servicios/participantes.service';
import { CommonModule } from '@angular/common';

describe('CrearParticipantesComponent', () => {
  let component: CrearParticipantesComponent;
  let fixture: ComponentFixture<CrearParticipantesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CrearParticipantesComponent,
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

    fixture = TestBed.createComponent(CrearParticipantesComponent);
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