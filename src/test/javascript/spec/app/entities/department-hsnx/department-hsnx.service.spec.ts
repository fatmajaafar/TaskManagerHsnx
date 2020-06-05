import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DepartmentHsnxService } from 'app/entities/department-hsnx/department-hsnx.service';
import { IDepartmentHsnx, DepartmentHsnx } from 'app/shared/model/department-hsnx.model';

describe('Service Tests', () => {
  describe('DepartmentHsnx Service', () => {
    let injector: TestBed;
    let service: DepartmentHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: IDepartmentHsnx;
    let expectedResult: IDepartmentHsnx | IDepartmentHsnx[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DepartmentHsnxService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new DepartmentHsnx(0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a DepartmentHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DepartmentHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a DepartmentHsnx', () => {
        const returnedFromService = Object.assign(
          {
            deptName: 'BBBBBB',
            deptNote: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of DepartmentHsnx', () => {
        const returnedFromService = Object.assign(
          {
            deptName: 'BBBBBB',
            deptNote: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a DepartmentHsnx', () => {
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
