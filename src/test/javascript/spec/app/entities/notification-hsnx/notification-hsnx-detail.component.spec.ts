import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaskManagerHsnxTestModule } from '../../../test.module';
import { NotificationHsnxDetailComponent } from 'app/entities/notification-hsnx/notification-hsnx-detail.component';
import { NotificationHsnx } from 'app/shared/model/notification-hsnx.model';

describe('Component Tests', () => {
  describe('NotificationHsnx Management Detail Component', () => {
    let comp: NotificationHsnxDetailComponent;
    let fixture: ComponentFixture<NotificationHsnxDetailComponent>;
    const route = ({ data: of({ notification: new NotificationHsnx(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TaskManagerHsnxTestModule],
        declarations: [NotificationHsnxDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NotificationHsnxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificationHsnxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notification on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notification).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
