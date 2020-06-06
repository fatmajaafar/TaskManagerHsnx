import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EmployeeHsnxService } from 'app/entities/employee-hsnx/employee-hsnx.service';
import { IEmployeeHsnx, EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';

describe('Service Tests', () => {
  describe('EmployeeHsnx Service', () => {
    let injector: TestBed;
    let service: EmployeeHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmployeeHsnx;
    let expectedResult: IEmployeeHsnx | IEmployeeHsnx[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EmployeeHsnxService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new EmployeeHsnx(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            branchHiredate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a EmployeeHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            branchHiredate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            branchHiredate: currentDate
          },
          returnedFromService
        );

        service.create(new EmployeeHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a EmployeeHsnx', () => {
        const returnedFromService = Object.assign(
          {
            branchName: 'BBBBBB',
            branchPhone: 'BBBBBB',
            branchFax: 'BBBBBB',
            branchAddress: 'BBBBBB',
            branchEmail: 'BBBBBB',
            branchHiredate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            branchHiredate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of EmployeeHsnx', () => {
        const returnedFromService = Object.assign(
          {
            branchName: 'BBBBBB',
            branchPhone: 'BBBBBB',
            branchFax: 'BBBBBB',
            branchAddress: 'BBBBBB',
            branchEmail: 'BBBBBB',
            branchHiredate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            branchHiredate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a EmployeeHsnx', () => {
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
