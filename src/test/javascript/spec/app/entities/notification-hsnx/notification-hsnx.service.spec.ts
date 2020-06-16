import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { NotificationHsnxService } from 'app/entities/notification-hsnx/notification-hsnx.service';
import { INotificationHsnx, NotificationHsnx } from 'app/shared/model/notification-hsnx.model';

describe('Service Tests', () => {
  describe('NotificationHsnx Service', () => {
    let injector: TestBed;
    let service: NotificationHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: INotificationHsnx;
    let expectedResult: INotificationHsnx | INotificationHsnx[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(NotificationHsnxService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new NotificationHsnx(0, 'AAAAAAA', false, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a NotificationHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new NotificationHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a NotificationHsnx', () => {
        const returnedFromService = Object.assign(
          {
            message: 'BBBBBB',
            handled: true,
            notifFrom: 'BBBBBB',
            notifto: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of NotificationHsnx', () => {
        const returnedFromService = Object.assign(
          {
            message: 'BBBBBB',
            handled: true,
            notifFrom: 'BBBBBB',
            notifto: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a NotificationHsnx', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
