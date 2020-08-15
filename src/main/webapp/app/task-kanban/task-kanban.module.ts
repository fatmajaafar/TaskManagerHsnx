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
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ButtonModule, CheckBoxModule, RadioButtonModule, SwitchModule } from '@syncfusion/ej2-angular-buttons';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material';
import { MatTabsModule } from '@angular/material';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';

@NgModule({
  declarations: [TaskKanbanComponent],
  imports: [
    CommonModule,
    HttpModule,
    KanbanAllModule,
    DialogModule,
    NgbModule,
    CheckBoxAllModule,
    DropDownListAllModule,
    NumericTextBoxAllModule,
    TextBoxAllModule,
    FontAwesomeModule,
    ButtonModule,
    CheckBoxModule,
    RadioButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatToolbarModule,
    MatCardModule,
    MatSelectModule,
    MatChipsModule,
    SwitchModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserModule,
    RouterModule.forChild([{ path: 'task-kanban', component: TaskKanbanComponent }])
  ],
  providers: [],
  bootstrap: [TaskKanbanComponent]
})
export class TaskKanbanModule {}
