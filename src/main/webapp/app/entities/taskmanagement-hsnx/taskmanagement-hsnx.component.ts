import { Component, OnInit } from '@angular/core';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { HttpResponse } from '@angular/common/http';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import { EventHsnxService } from '../event-hsnx/event-hsnx.service';
import { IEventHsnx } from 'app/shared/model/event-hsnx.model';
import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from '../notification-hsnx/notification-hsnx.service';

@Component({
  selector: 'jhi-taskmanagement-hsnx',
  templateUrl: './taskmanagement-hsnx.component.html',
  styleUrls: ['./taskmanagement-hsnx.component.scss']
})
export class TaskmanagementHsnxComponent implements OnInit {
  tasks: ITaskHsnx[] = [];
  events: IEventHsnx[] = [];
  notifications: INotificationHsnx[] = [];

  constructor(
    protected taskService: TaskHsnxService,
    protected eventService: EventHsnxService,
    protected notificationService: NotificationHsnxService
  ) {}

  getBackgroundColor(event: Event): String {
    return event ? '#ffefc8' : '';
  }

  ngOnInit(): void {
    this.taskService
      .query({
        size: 1000
      })
      .subscribe(
        (res: HttpResponse<ITaskHsnx[]>) => {
          this.tasks = res.body || [];
        },
        () => ''
      );

    this.notificationService.query({
      size: 1000
    });

    this.eventService

      .query({
        size: 1000
      })

      .subscribe(
        (res: HttpResponse<IEventHsnx[]>) => {
          this.events = res.body || [];
        },
        () => ''
      );
  }
}
