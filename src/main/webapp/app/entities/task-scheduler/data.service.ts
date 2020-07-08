/* eslint-disable */
import { Injectable } from '@angular/core';
import { DayPilot } from 'daypilot-pro-angular';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import * as moment from 'moment';

@Injectable()
export class DataService {
  resources: any[] = [
    { name: 'Resource 1', id: 'R1' },
    { name: 'Resource 2', id: 'R2' }
  ];

  events: ITaskEventHsnx[] = [];

  constructor(protected taskService: TaskHsnxService, private http: HttpClient) {}

  getEvents(from: string, to: string): Observable<any[]> {
    this.events = [];

    this.taskService
      .query({
        'dateStart.greaterOrEqualThan': from,
        'dateEnd.lessOrEqualThan': to,
        size: 1000
      })
      .subscribe(
        (res: HttpResponse<ITaskHsnx[]>) => {
          if (res.body) {
            let i = 0;

            res.body.forEach(element => {
              i += 1;

              const task: ITaskEventHsnx = {};
              task.id = i.toString();
              task.resource = 'R2';
              task.start = moment(element.dateStart).format('YYYY-MM-DD');
              task.end = moment(element.dateEnd).format('YYYY-MM-DD');
              task.text = element.taskdescription;
              task.color = '#e69138';

              this.events.push(task);
            });
          }
        },
        () => ''
      );
    // simulating an HTTP request
    return new Observable(observer => {
      setTimeout(() => {
        observer.next(this.events);
      }, 200);
    });

    // return this.http.get("/api/events?from=" + from.toString() + "&to=" + to.toString());
  }

  getResources(): Observable<any[]> {
    // simulating an HTTP request
    return new Observable(observer => {
      setTimeout(() => {
        observer.next(this.resources);
      }, 200);
    });

    // return this.http.get("/api/resources");
  }
}

export interface ITaskEventHsnx {
  id?: string;
  resource?: string;
  start?: string;
  end?: string;
  text?: string;
  color?: string;
}
