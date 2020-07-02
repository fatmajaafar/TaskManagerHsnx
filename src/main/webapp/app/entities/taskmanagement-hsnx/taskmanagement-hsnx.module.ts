import { NgModule } from '@angular/core';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { TaskmanagementHsnxComponent } from './taskmanagement-hsnx.component';
import { taskManagementRoute } from './taskmanagement-hsnx.route';
import { RouterModule } from '@angular/router';
import { TaskFormComponent } from './task-form.component';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(taskManagementRoute)],
  declarations: [TaskmanagementHsnxComponent, TaskFormComponent],
  entryComponents: [TaskFormComponent]
})
export class TaskManagerHsnxTaskmanagementHsnxModule {}
