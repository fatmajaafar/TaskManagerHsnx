import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';

type EntityResponseType = HttpResponse<ITaskHsnx>;
type EntityArrayResponseType = HttpResponse<ITaskHsnx[]>;

@Injectable({ providedIn: 'root' })
export class TaskHsnxService {
  public resourceUrl = SERVER_API_URL + 'api/tasks';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/tasks';

  constructor(protected http: HttpClient) {}

  create(task: ITaskHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .post<ITaskHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(task: ITaskHsnx): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .put<ITaskHsnx>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaskHsnx>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaskHsnx[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaskHsnx[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(task: ITaskHsnx): ITaskHsnx {
    const copy: ITaskHsnx = Object.assign({}, task, {
      dateStart: task.dateStart && task.dateStart.isValid() ? task.dateStart.format(DATE_FORMAT) : undefined,
      timeStart: task.timeStart && task.timeStart.isValid() ? task.timeStart.toJSON() : undefined,
      dateEnd: task.dateEnd && task.dateEnd.isValid() ? task.dateEnd.format(DATE_FORMAT) : undefined,
      timeEnd: task.timeEnd && task.timeEnd.isValid() ? task.timeEnd.toJSON() : undefined,
      DueDate: task.DueDate && task.DueDate.isValid() ? task.DueDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateStart = res.body.dateStart ? moment(res.body.dateStart) : undefined;
      res.body.timeStart = res.body.timeStart ? moment(res.body.timeStart) : undefined;
      res.body.dateEnd = res.body.dateEnd ? moment(res.body.dateEnd) : undefined;
      res.body.timeEnd = res.body.timeEnd ? moment(res.body.timeEnd) : undefined;
      res.body.DueDate = res.body.DueDate ? moment(res.body.DueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((task: ITaskHsnx) => {
        task.dateStart = task.dateStart ? moment(task.dateStart) : undefined;
        task.timeStart = task.timeStart ? moment(task.timeStart) : undefined;
        task.dateEnd = task.dateEnd ? moment(task.dateEnd) : undefined;
        task.timeEnd = task.timeEnd ? moment(task.timeEnd) : undefined;
        task.DueDate = task.DueDate ? moment(task.DueDate) : undefined;
      });
    }
    return res;
  }
}
