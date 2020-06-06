import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { EventHsnxUpdateComponent } from 'app/entities/event-hsnx/event-hsnx-update.component';
import { EventHsnxService } from 'app/entities/event-hsnx/event-hsnx.service';
import { EventHsnx } from 'app/shared/model/event-hsnx.model';

describe('Component Tests', () => {
  describe('EventHsnx Management Update Component', () => {
    let comp: EventHsnxUpdateComponent;
    let fixture: ComponentFixture<EventHsnxUpdateComponent>;
    let service: EventHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [EventHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EventHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventHsnx(123);
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
        const entity = new EventHsnx();
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
