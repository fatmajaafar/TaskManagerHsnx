import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { JobHsnxComponent } from './job-hsnx.component';
import { JobHsnxDetailComponent } from './job-hsnx-detail.component';
import { JobHsnxUpdateComponent } from './job-hsnx-update.component';
import { JobHsnxDeleteDialogComponent } from './job-hsnx-delete-dialog.component';
import { jobRoute } from './job-hsnx.route';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(jobRoute), ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule],
  declarations: [JobHsnxComponent, JobHsnxDetailComponent, JobHsnxUpdateComponent, JobHsnxDeleteDialogComponent],
  entryComponents: [JobHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxJobHsnxModule {}
