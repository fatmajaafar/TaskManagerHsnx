import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { DepartmentHsnxComponent } from './department-hsnx.component';
import { DepartmentHsnxDetailComponent } from './department-hsnx-detail.component';
import { DepartmentHsnxUpdateComponent } from './department-hsnx-update.component';
import { DepartmentHsnxDeleteDialogComponent } from './department-hsnx-delete-dialog.component';
import { departmentRoute } from './department-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(departmentRoute)],
  declarations: [
    DepartmentHsnxComponent,
    DepartmentHsnxDetailComponent,
    DepartmentHsnxUpdateComponent,
    DepartmentHsnxDeleteDialogComponent
  ],
  entryComponents: [DepartmentHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxDepartmentHsnxModule {}
