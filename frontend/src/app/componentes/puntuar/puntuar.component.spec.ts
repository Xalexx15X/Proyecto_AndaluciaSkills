import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntuarComponent } from './puntuar.component';

describe('PuntuarComponent', () => {
  let component: PuntuarComponent;
  let fixture: ComponentFixture<PuntuarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuntuarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PuntuarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
