/*eslint-disable*/
import { Component, ViewChild, ViewEncapsulation } from '@angular/core';
import { addClass } from '@syncfusion/ej2-base';
import {
  KanbanComponent,
  ColumnsModel,
  CardSettingsModel,
  SwimlaneSettingsModel,
  DialogSettingsModel,
  CardRenderedEventArgs
} from '@syncfusion/ej2-angular-kanban';
import { ButtonComponent } from '@syncfusion/ej2-angular-buttons';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { EmployeeHsnxService } from 'app/entities/employee-hsnx/employee-hsnx.service';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { IKanbanData } from './data';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
//import { IKanbanData, cardData } from './data';
@Component({
  selector: 'jhi-task-kanban',
  templateUrl: './task-kanban.component.html',
  styleUrls: ['./task-kanban.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TaskKanbanComponent {
  @ViewChild('kanbanObj', { static: true }) kanbanObj!: KanbanComponent;
  @ViewChild('toggleBtn', { static: true })
  public toggleBtn!: ButtonComponent;
  public kanbanData: IKanbanData[] = []; //extend([], kanbanData, [], true) as Object[];
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

  constructor(protected taskService: TaskHsnxService, private http: HttpClient, protected employeeService: EmployeeHsnxService) {
    this.taskService.query({ size: 10000 }).subscribe((res: HttpResponse<ITaskHsnx[]>) => {
      if (res.body) {
        let task1: IKanbanData = {};
        this.kanbanData = [task1];
        this.kanbanObj.kanbanData.push(task1);

        let i = 0;
        res.body.forEach(element => {
          i += 1;
          let task: IKanbanData = {};
          task.Id = 'Task ' + i;
          task.Title = element.tasktitle;
          task.Summary = element.taskdescription;
          switch (element.taskstatus) {
            case 1:
              task.Status = 'Open';
              break;
            case 2:
              task.Status = 'InProgress';
              break;
            case 3:
              task.Status = 'Review';
              break;
            case 4:
              task.Status = 'Close';
              break;
          }

          task.RankId = element.id;
          task.Assignee = 'Andrew Fuller';

          this.kanbanData.push(task);
          this.kanbanObj.kanbanData.push(task);
        });
      }
    });
  }

  public getString(assignee: string) {
    //return assignee.match(/\b(\w)/g).join('').toUpperCase();
  }

  cardRendered(args: CardRenderedEventArgs): void {
    /*const val: string = (<{ [key: string]: Object }>args.data).Priority as string;
    addClass([args.element], val);*/
    if (args.data) {
      this.taskService
        .find(Number(args.data.RankId))
        .pipe
        //filter((mayBeOk: HttpResponse<ITaskHsnx>) => mayBeOk.ok),
        //map((response: HttpResponse<ITaskHsnx>) => response.body),
        ()
        .subscribe((res: HttpResponse<ITaskHsnx>) => {
          if (res.body) {
            let task: ITaskHsnx = res.body;
            switch (args.data?.Status) {
              case 'Open':
                task.taskstatus = 1;
                break;
              case 'InProgress':
                task.taskstatus = 2;
                break;
              case 'Review':
                task.taskstatus = 3;
                break;
              case 'Close':
                task.taskstatus = 4;
                break;
            }
            this.subscribeToSaveResponse(this.taskService.update(task));
          }
        });
    }
  }

  addClick(): void {
    const cardIds = this.kanbanObj.kanbanData.map((obj: { [key: string]: any }) => parseInt(obj.Id.replace('Task ', ''), 10));
    const cardCount: number = Math.max.apply(Math, cardIds) + 1;
    const cardDetails = {
      Id: 'Task ' + cardCount,
      Status: 'Open',
      Priority: 'Normal',
      Assignee: 'Andrew Fuller',
      Estimate: 0,
      Tags: '',
      Summary: ''
    };
    this.kanbanObj.openDialog('Add', cardDetails);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      () => '',
      () => ''
    );
  }
}
