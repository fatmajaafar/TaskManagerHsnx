import { DataService } from './data.service';
import { NgModule } from '@angular/core';

import { DayPilotModule } from 'daypilot-pro-angular';
import { TaskSchedulerComponent } from './task-scheduler.component';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { taskSchedulerRoute } from './task-scheduler.route';

@NgModule({
  imports: [DayPilotModule, RouterModule.forChild(taskSchedulerRoute)],
  declarations: [TaskSchedulerComponent],
  exports: [],
  providers: [DataService]
})
export class TaskSchedulerModule {}
