import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { TaskHsnxComponent } from './task-hsnx.component';
import { TaskHsnxDetailComponent } from './task-hsnx-detail.component';
import { TaskHsnxUpdateComponent } from './task-hsnx-update.component';
import { TaskHsnxDeleteDialogComponent } from './task-hsnx-delete-dialog.component';
import { taskRoute } from './task-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(taskRoute)],
  declarations: [TaskHsnxComponent, TaskHsnxDetailComponent, TaskHsnxUpdateComponent, TaskHsnxDeleteDialogComponent],
  entryComponents: [TaskHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxTaskHsnxModule {}
