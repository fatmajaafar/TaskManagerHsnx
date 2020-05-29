import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IDepartmentHsnx } from 'app/shared/model/department-hsnx.model';

type EntityResponseType = HttpResponse<IDepartmentHsnx>;
type EntityArrayResponseType = HttpResponse<IDepartmentHsnx[]>;

@Injectable({ providedIn: 'root' })
export class DepartmentHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/departments';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/departments';

  constructor(protected http: HttpClient) {}

  create(department: IDepartmentHsnx): Observable<EntityResponseType> {
    return this.http.post<IDepartmentHsnx>(this.resourceUrl, department, { observe: 'response' });
  }

  update(department: IDepartmentHsnx): Observable<EntityResponseType> {
    return this.http.put<IDepartmentHsnx>(this.resourceUrl, department, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartmentHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartmentHsnx[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartmentHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
