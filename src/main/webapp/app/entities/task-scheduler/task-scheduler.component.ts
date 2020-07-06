import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { DayPilot, DayPilotSchedulerComponent } from 'daypilot-pro-angular';
import { DataService } from './data.service';

@Component({
  selector: 'jhi-task-scheduler',
  template: `
    <daypilot-scheduler [config]="config" [events]="events" #scheduler></daypilot-scheduler>
  `
})
export class TaskSchedulerComponent implements AfterViewInit {
  @ViewChild('scheduler', { static: true })
  scheduler!: DayPilotSchedulerComponent;

  events: any[] = [];

  config: any = {
    treeEnabled: true,
    timeHeaders: [{ groupBy: 'Month' }, { groupBy: 'Day', format: 'd' }],
    scale: 'Day',
    days: 31,
    startDate: '2018-10-01',
    onTimeRangeSelected: (args: { start: any; end: any; resource: any }) => {
      const dp = this.scheduler.control;
      DayPilot.Modal.prompt('Create a new event:', 'Event 1').then(function(modal): void {
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
      this.scheduler.control.message('Event moved');
    },
    eventResizeHandling: 'Update',
    onEventResized: () => {
      this.scheduler.control.message('Event resized');
    },
    eventDeleteHandling: 'Update',
    onEventDeleted: () => {
      this.scheduler.control.message('Event deleted');
    }
  };
  control: any;

  constructor(private ds: DataService) {}

  ngAfterViewInit(): void {
    this.ds.getResources().subscribe(result => (this.config.resources = result));

    const from = this.scheduler.control.visibleStart();
    const to = this.scheduler.control.visibleEnd();
    this.ds.getEvents(from, to).subscribe(result => {
      this.events = result;
    });
  }
}
