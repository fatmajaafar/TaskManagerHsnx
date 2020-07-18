import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IJobHsnx } from 'app/shared/model/job-hsnx.model';

type EntityResponseType = HttpResponse<IJobHsnx>;
type EntityArrayResponseType = HttpResponse<IJobHsnx[]>;

@Injectable({ providedIn: 'root' })
export class JobHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/jobs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/jobs';

  constructor(protected http: HttpClient) {}

  create(job: IJobHsnx): Observable<EntityResponseType> {
    return this.http.post<IJobHsnx>(this.resourceUrl, job, { observe: 'response' });
  }

  update(job: IJobHsnx): Observable<EntityResponseType> {
    return this.http.put<IJobHsnx>(this.resourceUrl, job, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobHsnx[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
