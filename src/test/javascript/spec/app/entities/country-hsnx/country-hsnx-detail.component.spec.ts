import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { CountryHsnxDetailComponent } from 'app/entities/country-hsnx/country-hsnx-detail.component';
import { CountryHsnx } from 'app/shared/model/country-hsnx.model';

describe('Component Tests', () => {
  describe('CountryHsnx Management Detail Component', () => {
    let comp: CountryHsnxDetailComponent;
    let fixture: ComponentFixture<CountryHsnxDetailComponent>;
    const route = ({ data: of({ country: new CountryHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [CountryHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CountryHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CountryHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load country on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.country).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
