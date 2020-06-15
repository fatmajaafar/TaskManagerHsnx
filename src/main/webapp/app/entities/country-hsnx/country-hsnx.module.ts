import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { CountryHsnxComponent } from './country-hsnx.component';
import { CountryHsnxDetailComponent } from './country-hsnx-detail.component';
import { CountryHsnxUpdateComponent } from './country-hsnx-update.component';
import { CountryHsnxDeleteDialogComponent } from './country-hsnx-delete-dialog.component';
import { countryRoute } from './country-hsnx.route';

@NgModule({
  imports: [TaskManagerHsnxSharedModule, RouterModule.forChild(countryRoute)],
  declarations: [CountryHsnxComponent, CountryHsnxDetailComponent, CountryHsnxUpdateComponent, CountryHsnxDeleteDialogComponent],
  entryComponents: [CountryHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxCountryHsnxModule {}
