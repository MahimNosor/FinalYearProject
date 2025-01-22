import { TestBed } from '@angular/core/testing';

import { Judge0Service } from './judge0.service';

describe('Judge0Service', () => {
  let service: Judge0Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Judge0Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
