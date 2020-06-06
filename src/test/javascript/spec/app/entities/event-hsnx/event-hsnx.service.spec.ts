import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EventHsnxService } from 'app/entities/event-hsnx/event-hsnx.service';
import { IEventHsnx, EventHsnx } from 'app/shared/model/event-hsnx.model';

describe('Service Tests', () => {
  describe('EventHsnx Service', () => {
    let injector: TestBed;
    let service: EventHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: IEventHsnx;
    let expectedResult: IEventHsnx | IEventHsnx[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EventHsnxService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new EventHsnx(0, 'AAAAAAA', currentDate, currentDate, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            starttime: currentDate.format(DATE_TIME_FORMAT),
            startdate: currentDate.format(DATE_FORMAT),
            endtime: currentDate.format(DATE_TIME_FORMAT),
            enddate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a EventHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            starttime: currentDate.format(DATE_TIME_FORMAT),
            startdate: currentDate.format(DATE_FORMAT),
            endtime: currentDate.format(DATE_TIME_FORMAT),
            enddate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            starttime: currentDate,
            startdate: currentDate,
            endtime: currentDate,
            enddate: currentDate
          },
          returnedFromService
        );

        service.create(new EventHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a EventHsnx', () => {
        const returnedFromService = Object.assign(
          {
            Description: 'BBBBBB',
            starttime: currentDate.format(DATE_TIME_FORMAT),
            startdate: currentDate.format(DATE_FORMAT),
            endtime: currentDate.format(DATE_TIME_FORMAT),
            enddate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            starttime: currentDate,
            startdate: currentDate,
            endtime: currentDate,
            enddate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of EventHsnx', () => {
        const returnedFromService = Object.assign(
          {
            Description: 'BBBBBB',
            starttime: currentDate.format(DATE_TIME_FORMAT),
            startdate: currentDate.format(DATE_FORMAT),
            endtime: currentDate.format(DATE_TIME_FORMAT),
            enddate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            starttime: currentDate,
            startdate: currentDate,
            endtime: currentDate,
            enddate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a EventHsnx', () => {
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
