import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { DepartmentHsnxUpdateComponent } from 'app/entities/department-hsnx/department-hsnx-update.component';
import { DepartmentHsnxService } from 'app/entities/department-hsnx/department-hsnx.service';
import { DepartmentHsnx } from 'app/shared/model/department-hsnx.model';

describe('Component Tests', () => {
  describe('DepartmentHsnx Management Update Component', () => {
    let comp: DepartmentHsnxUpdateComponent;
    let fixture: ComponentFixture<DepartmentHsnxUpdateComponent>;
    let service: DepartmentHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [DepartmentHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DepartmentHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DepartmentHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DepartmentHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DepartmentHsnx(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new DepartmentHsnx();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
