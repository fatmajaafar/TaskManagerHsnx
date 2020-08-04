/*eslint-disable*/
import { Component, ViewChild, ViewEncapsulation, Inject } from '@angular/core';
import { extend, addClass } from '@syncfusion/ej2-base';
import {
  KanbanComponent,
  ColumnsModel,
  CardSettingsModel,
  SwimlaneSettingsModel,
  DialogSettingsModel,
  CardRenderedEventArgs
} from '@syncfusion/ej2-angular-kanban';
import { cardData } from './data';

@Component({
  selector: 'jhi-task-kanban',
  templateUrl: './task-kanban.component.html',
  styleUrls: ['./task-kanban.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TaskKanbanComponent {
  @ViewChild('kanbanObj', { static: true }) kanbanObj!: KanbanComponent;
  public kanbanData: Object[] = extend([], cardData, [], true) as Object[];
  public columns: ColumnsModel[] = [
    { headerText: 'To Do', keyField: 'Open', allowToggle: true },
    { headerText: 'In Progress', keyField: 'InProgress', allowToggle: true },
    { headerText: 'In Review', keyField: 'Review', allowToggle: true },
    { headerText: 'Done', keyField: 'Close', allowToggle: true }
  ];
  public cardSettings: CardSettingsModel = {
    headerField: 'Title',
    template: '#cardTemplate',
    selectionType: 'Multiple'
  };
  public dialogSettings: DialogSettingsModel = {
    fields: [
      { text: 'ID', key: 'Title', type: 'TextBox' },
      { key: 'Status', type: 'DropDown' },
      { key: 'Assignee', type: 'DropDown' },
      { key: 'RankId', type: 'TextBox' },
      { key: 'Summary', type: 'TextArea' }
    ]
  };
  public swimlaneSettings: SwimlaneSettingsModel = { keyField: 'Assignee' };

  constructor() {}

  public getString(assignee: string) {
    /*
        return assignee.match(/\b(\w)/g).join('').toUpperCase();*/
  }
  cardRendered(args: CardRenderedEventArgs): void {
    const val: string = (<{ [key: string]: Object }>args.data).Priority as string;
    addClass([args.element], val);
  }
}
