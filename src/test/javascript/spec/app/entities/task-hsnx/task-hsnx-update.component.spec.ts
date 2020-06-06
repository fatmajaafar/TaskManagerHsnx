import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { TaskHsnxUpdateComponent } from 'app/entities/task-hsnx/task-hsnx-update.component';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';
import { TaskHsnx } from 'app/shared/model/task-hsnx.model';

describe('Component Tests', () => {
  describe('TaskHsnx Management Update Component', () => {
    let comp: TaskHsnxUpdateComponent;
    let fixture: ComponentFixture<TaskHsnxUpdateComponent>;
    let service: TaskHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [TaskHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TaskHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskHsnx(123);
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
        const entity = new TaskHsnx();
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
