/* eslint-disable */
import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { DayPilot, DayPilotSchedulerComponent } from 'daypilot-pro-angular';
import { DataService } from './data.service';
import * as moment from 'moment';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

@Component({
  selector: 'jhi-task-scheduler',
  templateUrl: './task-scheduler.component.html',
  styleUrls: ['task-scheduler.component.scss']
})
export class TaskSchedulerComponent implements AfterViewInit {
  @ViewChild('scheduler', { static: true })
  scheduler!: DayPilotSchedulerComponent;

  events: any[] = [];

  fromDate = moment(new Date().setDate(1)).format('YYYY-MM-DD');
  toDate = moment(new Date()).format('YYYY-MM-DD');

  config: any = {
    treeEnabled: true,
    timeHeaders: [{ groupBy: 'Month' }, { groupBy: 'Day', format: 'd' }],
    scale: 'Day',
    days: 31,
    startDate: this.fromDate,
    endDate: this.toDate,

    onTimeRangeSelected: (args: { start: any; end: any; resource: any }) => {
      const dp = this.scheduler.control;
      DayPilot.Modal.prompt('Create a new task:', 'Task 1').then((modal): void => {
        dp.clearSelection();
        if (!modal.result) {
          return;
        }
        dp.events.add(
          new DayPilot.Event({
            start: args.start,
            end: args.end,
            id: DayPilot.guid(),
            resource: args.resource,
            text: modal.result
          })
        );

        const task: ITaskHsnx = {};
        task.dateStart = moment(args.start, DATE_FORMAT);
        task.dateEnd = moment(args.end, DATE_FORMAT);
        task.taskdescription = modal.result;
        task.tasktitle = 'Task ' + args.start;

        this.subscribeToSaveResponseTask(this.taskService.create(task));
      });
    },
    eventMoveHandling: 'Update',
    onEventMoved: (res: any) => {
      console.clear();
      console.log(res.e.data);
      this.taskService.find(res.e.data.id).subscribe(
        (resTask: HttpResponse<ITaskHsnx>) => {
          if (resTask.body) {
            resTask.body.dateStart = moment(res.e.data.start, DATE_FORMAT);
            resTask.body.dateEnd = moment(res.e.data.end, DATE_FORMAT);
            this.subscribeToSaveResponseTask(this.taskService.update(resTask.body));
          }
        },
        () => ''
      );
      this.scheduler.control.message('Task moved');
    },
    eventResizeHandling: 'Update',
    onEventResized: (res: any) => {
      this.taskService.find(res.e.data.id).subscribe(
        (resTask: HttpResponse<ITaskHsnx>) => {
          if (resTask.body) {
            resTask.body.dateStart = moment(res.e.data.start, DATE_FORMAT);
            resTask.body.dateEnd = moment(res.e.data.end, DATE_FORMAT);
            this.subscribeToSaveResponseTask(this.taskService.update(resTask.body));
          }
        },
        () => ''
      );
      this.scheduler.control.message('Task resized');
    },
    eventDeleteHandling: 'Delete',
    onEventDeleted: (res: any) => {
      this.taskService.delete(res.e.data.id).subscribe(() => this.load());
      this.scheduler.control.message('Task deleted');
    }
  };
  control: any;

  constructor(protected taskService: TaskHsnxService, private ds: DataService) {}

  ngAfterViewInit(): void {
    this.load();
  }

  load() {
    this.ds.getResources().subscribe(result => {
      this.config.resources = result;
      this.config.startDate = this.fromDate;
      this.config.endDate = this.toDate;

      this.ds.getEvents(this.fromDate, this.toDate).subscribe(result => {
        this.events = result;
      });
    });
  }

  protected subscribeToSaveResponseTask(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      (res: HttpResponse<ITaskHsnx>) => this.load(),
      () => ''
    );
  }
}
