import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { DepartmentHsnxDetailComponent } from 'app/entities/department-hsnx/department-hsnx-detail.component';
import { DepartmentHsnx } from 'app/shared/model/department-hsnx.model';

describe('Component Tests', () => {
  describe('DepartmentHsnx Management Detail Component', () => {
    let comp: DepartmentHsnxDetailComponent;
    let fixture: ComponentFixture<DepartmentHsnxDetailComponent>;
    const route = ({ data: of({ department: new DepartmentHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [DepartmentHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DepartmentHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DepartmentHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load department on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.department).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
