import { DataService } from './data.service';
import { NgModule } from '@angular/core';

import { DayPilotModule } from 'daypilot-pro-angular';
import { TaskSchedulerComponent } from './task-scheduler.component';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { taskSchedulerRoute } from './task-scheduler.route';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [DayPilotModule, RouterModule.forChild(taskSchedulerRoute), FormsModule],
  declarations: [TaskSchedulerComponent],
  exports: [],
  providers: [DataService]
})
export class TaskSchedulerModule {}
