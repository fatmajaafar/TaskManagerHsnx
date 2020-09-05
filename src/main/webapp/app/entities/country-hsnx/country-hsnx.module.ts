import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { CountryHsnxComponent } from './country-hsnx.component';
import { CountryHsnxDetailComponent } from './country-hsnx-detail.component';
import { CountryHsnxUpdateComponent } from './country-hsnx-update.component';
import { CountryHsnxDeleteDialogComponent } from './country-hsnx-delete-dialog.component';
import { countryRoute } from './country-hsnx.route';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';

@NgModule({
  imports: [
    TaskManagerHsnxSharedModule,
    RouterModule.forChild(countryRoute),
    ButtonModule,
    CheckBoxModule,
    RadioButtonModule,
    SwitchModule
  ],
  declarations: [CountryHsnxComponent, CountryHsnxDetailComponent, CountryHsnxUpdateComponent, CountryHsnxDeleteDialogComponent],
  entryComponents: [CountryHsnxDeleteDialogComponent]
})
export class TaskManagerHsnxCountryHsnxModule {}
