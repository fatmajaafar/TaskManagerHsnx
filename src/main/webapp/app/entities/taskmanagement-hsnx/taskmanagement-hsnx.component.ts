/*eslint-disable*/
import { Component, OnInit } from '@angular/core';
import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { HttpResponse } from '@angular/common/http';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import { EventHsnxService } from '../event-hsnx/event-hsnx.service';
import { IEventHsnx } from 'app/shared/model/event-hsnx.model';
import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from '../notification-hsnx/notification-hsnx.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/shared/constants/input.constants';
import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from '../employee-hsnx/employee-hsnx.service';
import { WebSocketService } from 'app/layouts/main/WebSocketService';

@Component({
  selector: 'jhi-taskmanagement-hsnx',
  templateUrl: './taskmanagement-hsnx.component.html',
  styleUrls: ['./taskmanagement-hsnx.component.scss']
})
export class TaskmanagementHsnxComponent implements OnInit {
  tasks: ITaskHsnx[] = [];
  events: IEventHsnx[] = [];
  notifications: INotificationHsnx[] = [];
  isSaving = false;
  dateStartDp: any;
  dateEndDp: any;
  dueDateDp: any;
  content: any;
  modalRef: any;
  Difference_In_Days: any;
  Difference_In_Day: any;
  employees: IEmployeeHsnx[] = [];
  //current date
  date = new FormControl(new Date());
  filter = '';
  employeeId = 0;
  constructor(
    protected modalService: NgbModal,
    protected taskService: TaskHsnxService,
    protected eventService: EventHsnxService,
    protected notificationService: NotificationHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected route: Router,
    protected employeeService: EmployeeHsnxService,
    protected webSocketService: WebSocketService
  ) {
    this.loadEvents();
  }
  loadEvents(): void {
    this.eventService

      .query({
        size: 1000
      })

      .subscribe(
        (res: HttpResponse<IEventHsnx[]>) => {
          this.events = res.body || [];
        },
        () => ''
      );
  }

  loadAllTasks(): void {
    if (this.employeeId) {
      this.taskService
        .query({
          'tblEmployeeId.equals': this.employeeId,
          'taskstatus.specified': true,
          size: 100000
        })
        .subscribe(
          (res: HttpResponse<ITaskHsnx[]>) => {
            const today = new Date();
            this.Difference_In_Days = 0;
            this.tasks = res.body || [];

            for (var i = 0; i < this.tasks.length; i++) {
              this.tasks[i].tblEmployeeId = this.employees[i].id;
              if (this.tasks[i].dueDate && this.tasks[i].taskstatus != 4) {
                var date = moment(this.tasks[i].dueDate).toDate();

                var Difference_In_Time = date.getTime() - today.getTime();
                this.Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
                //  return this.Difference_In_Days;
                //this.Difference_In_Day=this.Difference_In_Days;
                this.tasks[i].nbjrs = this.Difference_In_Days;
              }
            }
          },
          () => ''
        );
    } else {
      this.taskService
        .query({
          size: 100000
        })
        .subscribe(
          (res: HttpResponse<ITaskHsnx[]>) => {
            const today = new Date();
            this.Difference_In_Days = 0;
            this.tasks = res.body || [];

            for (var i = 0; i < this.tasks.length; i++) {
              if (this.tasks[i].dueDate && this.tasks[i].taskstatus != 4) {
                var date = moment(this.tasks[i].dueDate).toDate();

                var Difference_In_Time = date.getTime() - today.getTime();
                this.Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
                //  return this.Difference_In_Days;
                //this.Difference_In_Day=this.Difference_In_Days;
                this.tasks[i].nbjrs = this.Difference_In_Days;
              }
            }
          },
          () => ''
        );
    }
  }
  editForm = this.fb.group({
    id: [],
    tasktitle: [null, [Validators.required]],
    taskdescription: [],
    dateStart: ['', Validators.required],
    timeStart: [],
    dateEnd: [],
    timeEnd: [],
    taskstatus: [],
    taskpriority: [],
    dueDate: ['', Validators.required],
    taskcategory: [],
    taskstate: [],
    tblEmployeeId: []
  });

  getBackgroundColor(event: Event): String {
    return event ? '#f5e6e6' : '';
  }

  openDialog(content: any) {
    this.modalRef = this.modalService.open(content, { centered: true });
    this.employeeService.query({}).subscribe((res: HttpResponse<IEmployeeHsnx[]>) => {
      this.employees = [];
      if (res.body) {
        this.employees = res.body;
      }
    });
  }

  ngOnInit(): void {
    this.employeeService.query().subscribe((res: HttpResponse<IEmployeeHsnx[]>) => (this.employees = res.body || []));
    this.loadAllTasks();
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
      taskstatus: task.taskstatus,
      taskpriority: task.taskpriority,
      dueDate: task.dueDate,
      taskcategory: task.taskcategory,
      taskstate: task.taskstate,
      tblEmployeeId: task.tblEmployeeId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    task.taskstatus = 1;
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  private createFromForm(): ITaskHsnx {
    return {
      ...new TaskHsnx(),

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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      () => {
        this.modalRef.close();
      },
      () => this.onSaveError()
    );
  }
  protected subscribeToSaveResponseForClose(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      () => {
        this.modalRef.close();
      },
      () => ''
    );
  }
  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.route.navigate(['/task-hsnx']);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected convertDateFromClient(task: ITaskHsnx): ITaskHsnx {
    const copy: ITaskHsnx = Object.assign({}, task, {
      dateStart: task.dateStart && task.dateStart.isValid() ? task.dateStart.format(DATE_FORMAT) : undefined,
      timeStart: task.timeStart && task.timeStart.isValid() ? task.timeStart.toJSON() : undefined,
      dateEnd: task.dateEnd && task.dateEnd.isValid() ? task.dateEnd.format(DATE_FORMAT) : undefined,
      timeEnd: task.timeEnd && task.timeEnd.isValid() ? task.timeEnd.toJSON() : undefined,
      dueDate: task.dueDate && task.dueDate.isValid() ? task.dueDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }
}
