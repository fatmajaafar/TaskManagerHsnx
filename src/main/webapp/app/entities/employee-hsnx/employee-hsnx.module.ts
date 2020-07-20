import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { EmployeeHsnxComponent } from './employee-hsnx.component';
import { EmployeeHsnxDetailComponent } from './employee-hsnx-detail.component';
import { EmployeeHsnxUpdateComponent } from './employee-hsnx-update.component';
import { EmployeeHsnxDeleteDialogComponent } from './employee-hsnx-delete-dialog.component';
import { employeeRoute } from './employee-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(employeeRoute)],
  declarations: [EmployeeHsnxComponent, EmployeeHsnxDetailComponent, EmployeeHsnxUpdateComponent, EmployeeHsnxDeleteDialogComponent],
  entryComponents: [EmployeeHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxEmployeeHsnxModule {}
