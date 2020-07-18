import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { BranchHsnxUpdateComponent } from 'app/entities/branch-hsnx/branch-hsnx-update.component';
import { BranchHsnxService } from 'app/entities/branch-hsnx/branch-hsnx.service';
import { BranchHsnx } from 'app/shared/model/branch-hsnx.model';

describe('Component Tests', () => {
  describe('BranchHsnx Management Update Component', () => {
    let comp: BranchHsnxUpdateComponent;
    let fixture: ComponentFixture<BranchHsnxUpdateComponent>;
    let service: BranchHsnxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [BranchHsnxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BranchHsnxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BranchHsnxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BranchHsnxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BranchHsnx(123);
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
        const entity = new BranchHsnx();
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
