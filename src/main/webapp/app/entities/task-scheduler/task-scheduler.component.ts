/* eslint-disable */
import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { DayPilot, DayPilotSchedulerComponent } from 'daypilot-pro-angular';
import { DataService } from './data.service';
import * as moment from 'moment';

@Component({
  selector: 'jhi-task-scheduler',
  templateUrl: './task-scheduler.component.html'
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
      DayPilot.Modal.prompt('Create a new task:', 'Task 1').then(function(modal): void {
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
      });
    },
    eventMoveHandling: 'Update',
    onEventMoved: () => {
      this.scheduler.control.message('Task moved');
    },
    eventResizeHandling: 'Update',
    onEventResized: () => {
      this.scheduler.control.message('Task resized');
    },
    eventDeleteHandling: 'Update',
    onEventDeleted: () => {
      this.scheduler.control.message('Task deleted');
    }
  };
  control: any;

  constructor(private ds: DataService) {}

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
}
