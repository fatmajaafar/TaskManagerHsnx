import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IBranchHsnx } from 'app/shared/model/branch-hsnx.model';

type EntityResponseType = HttpResponse<IBranchHsnx>;
type EntityArrayResponseType = HttpResponse<IBranchHsnx[]>;

@Injectable({ providedIn: 'root' })
export class BranchHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/branches';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/branches';

  constructor(protected http: HttpClient) {}

  create(branch: IBranchHsnx): Observable<EntityResponseType> {
    return this.http.post<IBranchHsnx>(this.resourceUrl, branch, { observe: 'response' });
  }

  update(branch: IBranchHsnx): Observable<EntityResponseType> {
    return this.http.put<IBranchHsnx>(this.resourceUrl, branch, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBranchHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBranchHsnx[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBranchHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
