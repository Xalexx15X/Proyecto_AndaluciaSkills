import { TestBed } from '@angular/core/testing';

import { GanadoresService } from './ganador.service';

describe('GanadorService', () => {
  let service: GanadoresService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GanadoresService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
