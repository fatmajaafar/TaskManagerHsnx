import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICountryHsnx } from 'app/shared/model/country-hsnx.model';

type EntityResponseType = HttpResponse<ICountryHsnx>;
type EntityArrayResponseType = HttpResponse<ICountryHsnx[]>;

@Injectable({ providedIn: 'root' })
export class CountryHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/countries';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/countries';

  constructor(protected http: HttpClient) {}

  create(country: ICountryHsnx): Observable<EntityResponseType> {
    return this.http.post<ICountryHsnx>(this.resourceUrl, country, { observe: 'response' });
  }

  update(country: ICountryHsnx): Observable<EntityResponseType> {
    return this.http.put<ICountryHsnx>(this.resourceUrl, country, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICountryHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountryHsnx[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountryHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
