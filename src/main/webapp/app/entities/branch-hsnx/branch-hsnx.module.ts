import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { BranchHsnxComponent } from './branch-hsnx.component';
import { BranchHsnxDetailComponent } from './branch-hsnx-detail.component';
import { BranchHsnxUpdateComponent } from './branch-hsnx-update.component';
import { BranchHsnxDeleteDialogComponent } from './branch-hsnx-delete-dialog.component';
import { branchRoute } from './branch-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(branchRoute)],
  declarations: [BranchHsnxComponent, BranchHsnxDetailComponent, BranchHsnxUpdateComponent, BranchHsnxDeleteDialogComponent],
  entryComponents: [BranchHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxBranchHsnxModule {}
