import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';
import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';

describe('Service Tests', () => {
  describe('TaskHsnx Service', () => {
    let injector: TestBed;
    let service: TaskHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaskHsnx;
    let expectedResult: ITaskHsnx | ITaskHsnx[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TaskHsnxService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TaskHsnx(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, currentDate, currentDate, 0, 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateStart: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a TaskHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateStart: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            timeStart: currentDate,
            dateEnd: currentDate,
            timeEnd: currentDate,
            dueDate: currentDate
          },
          returnedFromService
        );

        service.create(new TaskHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a TaskHsnx', () => {
        const returnedFromService = Object.assign(
          {
            tasktitle: 'BBBBBB',
            taskdescription: 'BBBBBB',
            dateStart: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            taskstatus: 1,
            taskpriority: 1,
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            timeStart: currentDate,
            dateEnd: currentDate,
            timeEnd: currentDate,
            dueDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of TaskHsnx', () => {
        const returnedFromService = Object.assign(
          {
            tasktitle: 'BBBBBB',
            taskdescription: 'BBBBBB',
            dateStart: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            taskstatus: 1,
            taskpriority: 1,
            dueDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            timeStart: currentDate,
            dateEnd: currentDate,
            timeEnd: currentDate,
            dueDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a TaskHsnx', () => {
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
