import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { JobHsnxUpdateComponent } from 'app/entities/job-hsnx/job-hsnx-update.component';
import { JobHsnxService } from 'app/entities/job-hsnx/job-hsnx.service';
import { JobHsnx } from 'app/shared/model/job-hsnx.model';

describe('Component Tests', () => {
  describe('JobHsnx Management Update Component', () => {
    let comp: JobHsnxUpdateComponent;
    let fixture: ComponentFixture<JobHsnxUpdateComponent>;
    let service: JobHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [JobHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JobHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobHsnx(123);
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
        const entity = new JobHsnx();
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
