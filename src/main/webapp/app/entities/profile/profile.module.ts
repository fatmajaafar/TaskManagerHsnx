import { NgModule } from '@angular/core';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { ProfileComponent } from './profile.component';
import { profileRoute } from './profile.route';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(profileRoute)],
  declarations: [ProfileComponent]
})
export class TaskManagerHsnxProfileModule {}
