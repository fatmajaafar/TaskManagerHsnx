import { NgModule } from '@angular/core';

import { TaskManagerHsnxSharedModule } from 'app/shared/shared.module';
import { TaskmanagementHsnxComponent } from './taskmanagement-hsnx.component';
import { taskManagementRoute } from './taskmanagement-hsnx.route';
import { RouterModule } from '@angular/router';
import { TaskFormComponent } from './task-form.component';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';
import { NumericTextBoxAllModule, TextBoxAllModule } from '@syncfusion/ej2-angular-inputs';

import { DialogModule } from '@syncfusion/ej2-angular-popups';

import { DropDownListAllModule } from '@syncfusion/ej2-angular-dropdowns';

import { CheckBoxAllModule, ChipListModule } from '@syncfusion/ej2-angular-buttons';

import { KanbanAllModule } from '@syncfusion/ej2-angular-kanban';
import { SidebarModule, MenuAllModule, TreeViewAllModule } from '@syncfusion/ej2-angular-navigations';

@NgModule({
  imports: [
    TaskManagerHsnxSharedModule,
    RouterModule.forChild(taskManagementRoute),
    ButtonModule,
    CheckBoxModule,
    RadioButtonModule,
    SwitchModule,
    NumericTextBoxAllModule,
    TextBoxAllModule,
    DialogModule,
    DropDownListAllModule,
    CheckBoxAllModule,
    ChipListModule,
    KanbanAllModule,
    SidebarModule,
    MenuAllModule,
    TreeViewAllModule
  ],
  declarations: [TaskmanagementHsnxComponent, TaskFormComponent],
  entryComponents: [TaskFormComponent]
})
export class TaskManagerHsnxTaskmanagementHsnxModule {}
