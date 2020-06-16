import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { NotificationHsnxComponent } from './notification-hsnx.component';
import { NotificationHsnxDetailComponent } from './notification-hsnx-detail.component';
import { NotificationHsnxUpdateComponent } from './notification-hsnx-update.component';
import { NotificationHsnxDeleteDialogComponent } from './notification-hsnx-delete-dialog.component';
import { notificationRoute } from './notification-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(notificationRoute)],
  declarations: [
    NotificationHsnxComponent,
    NotificationHsnxDetailComponent,
    NotificationHsnxUpdateComponent,
    NotificationHsnxDeleteDialogComponent
  ],
  entryComponents: [NotificationHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxNotificationHsnxModule {}
