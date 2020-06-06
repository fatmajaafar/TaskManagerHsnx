import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { TaskHsnxDetailComponent } from 'app/entities/task-hsnx/task-hsnx-detail.component';
import { TaskHsnx } from 'app/shared/model/task-hsnx.model';

describe('Component Tests', () => {
  describe('TaskHsnx Management Detail Component', () => {
    let comp: TaskHsnxDetailComponent;
    let fixture: ComponentFixture<TaskHsnxDetailComponent>;
    const route = ({ data: of({ task: new TaskHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [TaskHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TaskHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaskHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load task on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.task).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
