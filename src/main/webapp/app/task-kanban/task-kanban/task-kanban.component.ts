/*eslint-disable*/
import { Component, ViewChild, ViewEncapsulation, OnInit } from '@angular/core';
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
import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { IKanbanData, IEmployeeData } from './data';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { FormBuilder, Validators } from '@angular/forms';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';

//import { IKanbanData, cardData } from './data';
@Component({
  selector: 'jhi-task-kanban',
  templateUrl: './task-kanban.component.html',
  styleUrls: ['./task-kanban.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TaskKanbanComponent implements OnInit {
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
  public status: string[] = ['To Do', 'In Progress', 'In Review', 'Done'];
  addTask: ITaskHsnx = {};
  editTask: ITaskHsnx = {};

  isSaving = false;
  employees: IEmployeeHsnx[] = [];
  task: ITaskHsnx = {};

  public swimlaneSettings: SwimlaneSettingsModel = { keyField: 'Assignee' };
  modalRef: any;

  constructor(
    protected taskService: TaskHsnxService,
    protected http: HttpClient,
    protected employeeService: EmployeeHsnxService,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute
  ) {
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
          task.Assignee = element.tblEmployeeEmployeename;

          this.kanbanData.push(task);
          this.kanbanObj.kanbanData.push(task);
        });
      }
    });
  }

  ngOnInit(): void {}

  public getString(assignee: string) {
    //return assignee.match(/\b(\w)/g).join('').toUpperCase();
  }

  cardRendered(args: CardRenderedEventArgs): void {
    /*const val: string = (<{ [key: string]: Object }>args.data).Priority as string;
    addClass([args.element], val);*/
    if (args.data) {
      this.taskService
        .find(Number(args.data.RankId))
        .pipe(
          filter((mayBeOk: HttpResponse<ITaskHsnx>) => mayBeOk.ok),
          map((response: HttpResponse<ITaskHsnx>) => response.body)
        )
        .subscribe(res => {
          if (res) {
            let task: ITaskHsnx = res;
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

  //OPEN ADD TASK MODAL
  addClick(cont: any) {
    this.modalRef = this.modalService.open(cont, { size: 'sm' });

    this.employeeService.query({}).subscribe((res: HttpResponse<IEmployeeHsnx[]>) => {
      this.employees = [];
      if (res.body) {
        this.employees = res.body;
      }
    });
  }

  /**save added task */
  savetask(): void {
    this.isSaving = true;
    const task = this.createFromForm();

    this.subscribeToSaveResponse(this.taskService.create(task));
  }

  // OPEN EDIT TASK MODAL
  open(content: any, data: IKanbanData) {
    this.editTask.id = data.RankId;
    this.editTask.tasktitle = data.Title;
    this.editTask.taskdescription = data.Summary;
    this.editTask.tblEmployeeEmployeename = data.Assignee;

    console.clear();
    console.log(this.editTask);
    this.modalService.open(content, { size: 'sm' });
    /*const task = this.editTask
    this.updateForm(data);*/
    this.employeeService.query({}).subscribe((res: HttpResponse<IEmployeeHsnx[]>) => {
      this.employees = [];
      if (res.body) {
        this.employees = res.body;
      }
    });
  }

  editForm = this.fb.group({
    id: [],
    tasktitle: [null, [Validators.required]],
    taskdescription: [],
    dateStart: [],
    timeStart: [],
    dateEnd: [],
    timeEnd: [],
    taskstatus: [],
    taskpriority: [],
    dueDate: [],
    taskcategory: [],
    taskstate: [],
    tblEmployeeId: []
  });

  private createFromForm(): ITaskHsnx {
    return {
      ...new TaskHsnx(),
      id: this.editForm.get(['id'])!.value,
      tasktitle: this.editForm.get(['tasktitle'])!.value,
      taskdescription: this.editForm.get(['taskdescription'])!.value,
      dateStart: this.editForm.get(['dateStart'])!.value,
      timeStart: this.editForm.get(['timeStart'])!.value ? moment(this.editForm.get(['timeStart'])!.value, DATE_TIME_FORMAT) : undefined,
      dateEnd: this.editForm.get(['dateEnd'])!.value,
      timeEnd: this.editForm.get(['timeEnd'])!.value ? moment(this.editForm.get(['timeEnd'])!.value, DATE_TIME_FORMAT) : undefined,
      taskstatus: this.editForm.get(['taskstatus'])!.value,
      taskpriority: this.editForm.get(['taskpriority'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      taskcategory: this.editForm.get(['taskcategory'])!.value,
      taskstate: this.editForm.get(['taskstate'])!.value,
      tblEmployeeId: this.editForm.get(['tblEmployeeId'])!.value
    };
  }

  updateForm(task: ITaskHsnx): void {
    this.editForm.patchValue({
      id: task.id,
      tasktitle: task.tasktitle,
      taskdescription: task.taskdescription,
      dateStart: task.dateStart,
      timeStart: task.timeStart ? task.timeStart.format(DATE_TIME_FORMAT) : null,
      dateEnd: task.dateEnd,
      timeEnd: task.timeEnd ? task.timeEnd.format(DATE_TIME_FORMAT) : null,
      tasksavestatus: task.taskstatus,
      taskpriority: task.taskpriority,
      dueDate: task.dueDate,
      taskcategory: task.taskcategory,
      taskstate: task.taskstate,
      tblEmployeeId: task.tblEmployeeId
    });
  }

  /**save task after edit */

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      () => '',
      () => '',
      this.modalRef.close()
    );
  }
}
