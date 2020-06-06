import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { EmployeeHsnxDetailComponent } from 'app/entities/employee-hsnx/employee-hsnx-detail.component';
import { EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';

describe('Component Tests', () => {
  describe('EmployeeHsnx Management Detail Component', () => {
    let comp: EmployeeHsnxDetailComponent;
    let fixture: ComponentFixture<EmployeeHsnxDetailComponent>;
    const route = ({ data: of({ employee: new EmployeeHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [EmployeeHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EmployeeHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmployeeHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load employee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.employee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
