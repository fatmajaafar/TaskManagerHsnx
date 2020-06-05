import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { ChartsModule } from 'ng2-charts';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild([HOME_ROUTE]), ChartsModule],
  declarations: [HomeComponent]
})
export class TaskManagerHsnxHomeModule {}
