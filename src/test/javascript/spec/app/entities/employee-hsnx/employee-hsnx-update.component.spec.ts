import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { EmployeeHsnxUpdateComponent } from 'app/entities/employee-hsnx/employee-hsnx-update.component';
import { EmployeeHsnxService } from 'app/entities/employee-hsnx/employee-hsnx.service';
import { EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';

describe('Component Tests', () => {
  describe('EmployeeHsnx Management Update Component', () => {
    let comp: EmployeeHsnxUpdateComponent;
    let fixture: ComponentFixture<EmployeeHsnxUpdateComponent>;
    let service: EmployeeHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [EmployeeHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EmployeeHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployeeHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployeeHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EmployeeHsnx(123);
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
        const entity = new EmployeeHsnx();
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
