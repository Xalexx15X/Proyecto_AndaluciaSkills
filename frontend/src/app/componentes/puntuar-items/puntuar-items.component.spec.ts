import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuntuarItemsComponent } from './puntuar-items.component';

describe('PuntuarItemsComponent', () => {
  let component: PuntuarItemsComponent;
  let fixture: ComponentFixture<PuntuarItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuntuarItemsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PuntuarItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
