/*eslint-disable*/
import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { HttpResponse } from '@angular/common/http';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';
import { EventHsnxService } from '../event-hsnx/event-hsnx.service';
import { IEventHsnx } from 'app/shared/model/event-hsnx.model';
import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from '../notification-hsnx/notification-hsnx.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TaskFormComponent } from './task-form.component';
import { SidebarComponent } from '@syncfusion/ej2-angular-navigations';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

@Component({
  selector: 'jhi-taskmanagement-hsnx',
  templateUrl: './taskmanagement-hsnx.component.html',
  styleUrls: ['./taskmanagement-hsnx.component.scss']
})
export class TaskmanagementHsnxComponent implements OnInit {
  @ViewChild('sidebar', { static: true })
  public sidebar!: SidebarComponent;
  public closeOnDocumentClick: boolean = false;
  tasks: ITaskHsnx[] = [];
  events: IEventHsnx[] = [];
  notifications: INotificationHsnx[] = [];
  isSaving = false;
  dateStartDp: any;
  dateEndDp: any;
  dueDateDp: any;
  content: any;

  modalRef: any;
  constructor(
    protected modalService: NgbModal,
    protected taskService: TaskHsnxService,
    protected eventService: EventHsnxService,
    protected notificationService: NotificationHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected route: Router
  ) {}
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
    taskstate: []
  });

  getBackgroundColor(event: Event): String {
    return event ? '#f5e6e6' : '';
  }

  openDialog(content: any) {
    this.modalRef = this.modalService.open(content, { centered: true });
  }

  toggleClick() {
    this.sidebar.toggle();
  }
  closeClick() {
    this.sidebar.hide();
  }
  openClick() {
    this.sidebar.show();
  }
  //To hide the sidebar element skelton during the page load by setting the visibity style when the control is created.
  onCreated(e: any): void {
    this.sidebar.element.style.visibility = 'visible';
  }

  /**
  getRandomColor():String {
   const color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  } */

  ngOnInit(): void {
    this.taskService
      .query({
        size: 1000
      })
      .subscribe(
        (res: HttpResponse<ITaskHsnx[]>) => {
          this.tasks = res.body || [];
        },
        () => ''
      );

    this.notificationService.query({
      size: 1000
    });

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
      taskstate: task.taskstate
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
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
      taskstate: this.editForm.get(['taskstate'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskHsnx>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
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
}
