import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CountryHsnxService } from 'app/entities/country-hsnx/country-hsnx.service';
import { ICountryHsnx, CountryHsnx } from 'app/shared/model/country-hsnx.model';

describe('Service Tests', () => {
  describe('CountryHsnx Service', () => {
    let injector: TestBed;
    let service: CountryHsnxService;
    let httpMock: HttpTestingController;
    let elemDefault: ICountryHsnx;
    let expectedResult: ICountryHsnx | ICountryHsnx[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CountryHsnxService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new CountryHsnx(0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
      });

      it('should create a CountryHsnx', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CountryHsnx()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
      });

      it('should update a CountryHsnx', () => {
        const returnedFromService = Object.assign(
          {
            countryName: 'BBBBBB',
            countryCode: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
      });

      it('should return a list of CountryHsnx', () => {
        const returnedFromService = Object.assign(
          {
            countryName: 'BBBBBB',
            countryCode: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
      });

      it('should delete a CountryHsnx', () => {
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
