import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { EventHsnxComponent } from './event-hsnx.component';
import { EventHsnxDetailComponent } from './event-hsnx-detail.component';
import { EventHsnxUpdateComponent } from './event-hsnx-update.component';
import { EventHsnxDeleteDialogComponent } from './event-hsnx-delete-dialog.component';
import { eventRoute } from './event-hsnx.route';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(eventRoute), ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule],
  declarations: [EventHsnxComponent, EventHsnxDetailComponent, EventHsnxUpdateComponent, EventHsnxDeleteDialogComponent],
  entryComponents: [EventHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxEventHsnxModule {}
