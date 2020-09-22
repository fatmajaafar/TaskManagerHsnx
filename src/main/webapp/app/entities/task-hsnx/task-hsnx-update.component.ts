import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators, FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from './task-hsnx.service';
import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from 'app/entities/employee-hsnx/employee-hsnx.service';

@Component({
  selector: 'jhi-task-hsnx-update',
  templateUrl: './task-hsnx-update.component.html'
})
export class TaskHsnxUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployeeHsnx[] = [];
  dateStartDp: any;
  dateEndDp: any;
  dueDateDp: any;
  task: ITaskHsnx[] = [];
  editForm = this.fb.group(
    {
      id: [],
      tasktitle: [null, [Validators.required]],
      taskdescription: [],
      dateStart: ['', Validators.required],
      timeStart: [],
      dateEnd: ['', Validators.required],
      timeEnd: [],
      taskstatus: [],
      taskpriority: [],
      dueDate: ['', Validators.required],
      taskcategory: [],
      taskstate: [],
      tblEmployeeId: []
    },
    { validator: this.dateLessThan('dateStart', 'dueDate') && this.dateLassThan2('dateStart', 'dateEnd') }
  );

  constructor(
    protected taskService: TaskHsnxService,
    protected EmployeeService: EmployeeHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      if (!task.id) {
        const today = moment().startOf('day');
        task.timeStart = today;
        task.timeEnd = today;
      }

      this.updateForm(task);

      this.EmployeeService.query().subscribe((res: HttpResponse<IEmployeeHsnx[]>) => (this.employees = res.body || []));
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

  trackById(index: number, item: IEmployeeHsnx): any {
    return item.id;
  }
  // compare dates
  dateLessThan(start: string, end: string) {
    return (group: FormGroup): { [key: string]: any } => {
      const s = group.controls[start];
      const e = group.controls[end];
      if (s.value > e.value) {
        return {
          dates: 'Due date should be less than Start date'
        };
      }
      return {};
    };
  }

  dateLassThan2(datestart: string, Dateend: string) {
    return (group: FormGroup): { [key: string]: any } => {
      const s = group.controls[datestart];
      const e = group.controls[Dateend];
      if (s.value > e.value) {
        return {
          dates: 'Due date should be less than Start date'
        };
      }
      return {};
    };
  }
}
