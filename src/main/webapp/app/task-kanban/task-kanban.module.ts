import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NumericTextBoxAllModule, TextBoxAllModule } from '@syncfusion/ej2-angular-inputs';

import { DialogModule } from '@syncfusion/ej2-angular-popups';

import { DropDownListAllModule } from '@syncfusion/ej2-angular-dropdowns';

import { CheckBoxAllModule } from '@syncfusion/ej2-angular-buttons';

import { KanbanAllModule } from '@syncfusion/ej2-angular-kanban';

import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';

import { TaskKanbanComponent } from './task-kanban/task-kanban.component';
@NgModule({
  declarations: [TaskKanbanComponent],
  imports: [
    CommonModule,
    HttpModule,
    KanbanAllModule,
    DialogModule,
    CheckBoxAllModule,
    DropDownListAllModule,
    NumericTextBoxAllModule,
    TextBoxAllModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserModule,
    RouterModule.forChild([{ path: 'task-kanban', component: TaskKanbanComponent }])
  ],
  providers: [],
  bootstrap: [TaskKanbanComponent]
})
export class TaskKanbanModule {}
