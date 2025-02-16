import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizarGanadoresComponent } from './visualizar-ganadores.component';

describe('VisualizarGanadoresComponent', () => {
  let component: VisualizarGanadoresComponent;
  let fixture: ComponentFixture<VisualizarGanadoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisualizarGanadoresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisualizarGanadoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
