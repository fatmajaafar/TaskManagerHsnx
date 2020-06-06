import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { EventHsnxComponent } from './event-hsnx.component';
import { EventHsnxDetailComponent } from './event-hsnx-detail.component';
import { EventHsnxUpdateComponent } from './event-hsnx-update.component';
import { EventHsnxDeleteDialogComponent } from './event-hsnx-delete-dialog.component';
import { eventRoute } from './event-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(eventRoute)],
  declarations: [EventHsnxComponent, EventHsnxDetailComponent, EventHsnxUpdateComponent, EventHsnxDeleteDialogComponent],
  entryComponents: [EventHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxEventHsnxModule {}
