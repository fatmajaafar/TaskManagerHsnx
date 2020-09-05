import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { NotificationHsnxComponent } from './notification-hsnx.component';
import { NotificationHsnxDetailComponent } from './notification-hsnx-detail.component';
import { NotificationHsnxUpdateComponent } from './notification-hsnx-update.component';
import { NotificationHsnxDeleteDialogComponent } from './notification-hsnx-delete-dialog.component';
import { notificationRoute } from './notification-hsnx.route';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';

@NgModule({
  imports: [
    TaskManagerHsnxSharedModule,
    RouterModule.forChild(notificationRoute),
    ButtonModule,
    CheckBoxModule,
    RadioButtonModule,
    SwitchModule
  ],
  declarations: [
    NotificationHsnxComponent,
    NotificationHsnxDetailComponent,
    NotificationHsnxUpdateComponent,
    NotificationHsnxDeleteDialogComponent
  ],
  entryComponents: [NotificationHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxNotificationHsnxModule {}
