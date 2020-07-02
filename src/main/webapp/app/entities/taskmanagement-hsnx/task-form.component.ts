import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from '../task-hsnx/task-hsnx.service';

@Component({
  selector: 'jhi-task-form',
  templateUrl: './task-form.component.html'
})
export class TaskFormComponent implements OnInit {
  isSaving = false;
  dateStartDp: any;
  dateEndDp: any;
  dueDateDp: any;

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

  constructor(protected taskService: TaskHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {
    this.dateStartDp = new Date().toString();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      if (!task.id) {
        const today = moment().startOf('day');
        task.timeStart = today;
        task.timeEnd = today;
        task.dateStartDp = today;
      }

      this.updateForm(task);
    });
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

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
