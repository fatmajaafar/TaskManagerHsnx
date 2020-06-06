import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEventHsnx } from 'app/shared/model/event-hsnx.model';

type EntityResponseType = HttpResponse<IEventHsnx>;
type EntityArrayResponseType = HttpResponse<IEventHsnx[]>;

@Injectable({ providedIn: 'root' })
export class EventHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/events';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/events';

  constructor(protected http: HttpClient) {}

  create(event: IEventHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .post<IEventHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(event: IEventHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(event);
    return this.http
      .put<IEventHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEventHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventHsnx[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(event: IEventHsnx): IEventHsnx {
    const copy: IEventHsnx = Object.assign({}, event, {
      starttime: event.starttime && event.starttime.isValid() ? event.starttime.toJSON() : undefined,
      startdate: event.startdate && event.startdate.isValid() ? event.startdate.format(DATE_FORMAT) : undefined,
      endtime: event.endtime && event.endtime.isValid() ? event.endtime.toJSON() : undefined,
      enddate: event.enddate && event.enddate.isValid() ? event.enddate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.starttime = res.body.starttime ? moment(res.body.starttime) : undefined;
      res.body.startdate = res.body.startdate ? moment(res.body.startdate) : undefined;
      res.body.endtime = res.body.endtime ? moment(res.body.endtime) : undefined;
      res.body.enddate = res.body.enddate ? moment(res.body.enddate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((event: IEventHsnx) => {
        event.starttime = event.starttime ? moment(event.starttime) : undefined;
        event.startdate = event.startdate ? moment(event.startdate) : undefined;
        event.endtime = event.endtime ? moment(event.endtime) : undefined;
        event.enddate = event.enddate ? moment(event.enddate) : undefined;
      });
    }
    return res;
  }
}
