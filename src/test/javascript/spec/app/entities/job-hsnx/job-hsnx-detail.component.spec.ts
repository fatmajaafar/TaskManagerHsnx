import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { JobHsnxDetailComponent } from 'app/entities/job-hsnx/job-hsnx-detail.component';
import { JobHsnx } from 'app/shared/model/job-hsnx.model';

describe('Component Tests', () => {
  describe('JobHsnx Management Detail Component', () => {
    let comp: JobHsnxDetailComponent;
    let fixture: ComponentFixture<JobHsnxDetailComponent>;
    const route = ({ data: of({ job: new JobHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [JobHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JobHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load job on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.job).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
