import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Ordres } from './ordres';

describe('Ordres', () => {
  let component: Ordres;
  let fixture: ComponentFixture<Ordres>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Ordres],
    }).compileComponents();

    fixture = TestBed.createComponent(Ordres);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
