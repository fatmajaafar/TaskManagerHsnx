import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { EventHsnxDetailComponent } from 'app/entities/event-hsnx/event-hsnx-detail.component';
import { EventHsnx } from 'app/shared/model/event-hsnx.model';

describe('Component Tests', () => {
  describe('EventHsnx Management Detail Component', () => {
    let comp: EventHsnxDetailComponent;
    let fixture: ComponentFixture<EventHsnxDetailComponent>;
    const route = ({ data: of({ event: new EventHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [EventHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EventHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load event on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.event).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
