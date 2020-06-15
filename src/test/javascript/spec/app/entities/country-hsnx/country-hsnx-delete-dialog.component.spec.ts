import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CountryHsnxDeleteDialogComponent } from 'app/entities/country-hsnx/country-hsnx-delete-dialog.component';
import { CountryHsnxService } from 'app/entities/country-hsnx/country-hsnx.service';

describe('Component Tests', () => {
  describe('CountryHsnx Management Delete Component', () => {
    let comp: CountryHsnxDeleteDialogComponent;
    let fixture: ComponentFixture<CountryHsnxDeleteDialogComponent>;
    let service: CountryHsnxService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [CountryHsnxDeleteDialogComponent]
      })
        .overrideTemplate(CountryHsnxDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CountryHsnxDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CountryHsnxService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
