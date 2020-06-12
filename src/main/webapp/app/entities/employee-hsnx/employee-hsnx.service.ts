import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';

type EntityResponseType = HttpResponse<IEmployeeHsnx>;
type EntityArrayResponseType = HttpResponse<IEmployeeHsnx[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/employees';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/employees';

  constructor(protected http: HttpClient) {}

  create(employee: IEmployeeHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .post<IEmployeeHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employee: IEmployeeHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .put<IEmployeeHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployeeHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeHsnx[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(employee: IEmployeeHsnx): IEmployeeHsnx {
    const copy: IEmployeeHsnx = Object.assign({}, employee, {
      employeehiredate:
        employee.employeehiredate && employee.employeehiredate.isValid() ? employee.employeehiredate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.employeehiredate = res.body.employeehiredate ? moment(res.body.employeehiredate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employee: IEmployeeHsnx) => {
        employee.employeehiredate = employee.employeehiredate ? moment(employee.employeehiredate) : undefined;
      });
    }
    return res;
  }
}
