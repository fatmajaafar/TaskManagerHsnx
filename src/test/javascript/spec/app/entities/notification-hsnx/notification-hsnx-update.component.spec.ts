import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { NotificationHsnxUpdateComponent } from 'app/entities/notification-hsnx/notification-hsnx-update.component';
import { NotificationHsnxService } from 'app/entities/notification-hsnx/notification-hsnx.service';
import { NotificationHsnx } from 'app/shared/model/notification-hsnx.model';

describe('Component Tests', () => {
  describe('NotificationHsnx Management Update Component', () => {
    let comp: NotificationHsnxUpdateComponent;
    let fixture: ComponentFixture<NotificationHsnxUpdateComponent>;
    let service: NotificationHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [NotificationHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NotificationHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NotificationHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NotificationHsnx(123);
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
        const entity = new NotificationHsnx();
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
