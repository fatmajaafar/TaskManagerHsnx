import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { BranchHsnxDetailComponent } from 'app/entities/branch-hsnx/branch-hsnx-detail.component';
import { BranchHsnx } from 'app/shared/model/branch-hsnx.model';

describe('Component Tests', () => {
  describe('BranchHsnx Management Detail Component', () => {
    let comp: BranchHsnxDetailComponent;
    let fixture: ComponentFixture<BranchHsnxDetailComponent>;
    const route = ({ data: of({ branch: new BranchHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [BranchHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BranchHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BranchHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load branch on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.branch).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
