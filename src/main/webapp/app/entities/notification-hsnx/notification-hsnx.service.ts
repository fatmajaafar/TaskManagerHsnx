import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';

type EntityResponseType = HttpResponse<INotificationHsnx>;
type EntityArrayResponseType = HttpResponse<INotificationHsnx[]>;

@Injectable({ providedIn: 'root' })
export class NotificationHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/notifications';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/notifications';

  constructor(protected http: HttpClient) {}

  create(notification: INotificationHsnx): Observable<EntityResponseType> {
    return this.http.post<INotificationHsnx>(this.resourceUrl, notification, { observe: 'response' });
  }

  update(notification: INotificationHsnx): Observable<EntityResponseType> {
    return this.http.put<INotificationHsnx>(this.resourceUrl, notification, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotificationHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotificationHsnx[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotificationHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
