import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEventHsnx, EventHsnx } from 'app/shared/model/event-hsnx.model';
import { EventHsnxService } from './event-hsnx.service';
import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from 'app/entities/task-hsnx/task-hsnx.service';

@Component({
  selector: 'jhi-event-hsnx-update',
  templateUrl: './event-hsnx-update.component.html'
})
export class EventHsnxUpdateComponent implements OnInit {
  isSaving = false;
  tasks: ITaskHsnx[] = [];
  startdateDp: any;
  enddateDp: any;

  editForm = this.fb.group({
    id: [],
    eventdescription: [null, [Validators.required]],
    starttime: [],
    startdate: [],
    endtime: [],
    enddate: [],
    tblTaskId: []
  });

  constructor(
    protected eventService: EventHsnxService,
    protected TaskService: TaskHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      if (!event.id) {
        const today = moment().startOf('day');
        event.starttime = today;
        event.endtime = today;
      }

      this.updateForm(event);

      this.TaskService.query().subscribe((res: HttpResponse<ITaskHsnx[]>) => (this.tasks = res.body || []));
    });
  }

  updateForm(event: IEventHsnx): void {
    this.editForm.patchValue({
      id: event.id,
      eventdescription: event.eventdescription,
      starttime: event.starttime ? event.starttime.format(DATE_TIME_FORMAT) : null,
      startdate: event.startdate,
      endtime: event.endtime ? event.endtime.format(DATE_TIME_FORMAT) : null,
      enddate: event.enddate,
      tblTaskId: event.tblTaskId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  private createFromForm(): IEventHsnx {
    return {
      ...new EventHsnx(),
      id: this.editForm.get(['id'])!.value,
      eventdescription: this.editForm.get(['eventdescription'])!.value,
      starttime: this.editForm.get(['starttime'])!.value ? moment(this.editForm.get(['starttime'])!.value, DATE_TIME_FORMAT) : undefined,
      startdate: this.editForm.get(['startdate'])!.value,
      endtime: this.editForm.get(['endtime'])!.value ? moment(this.editForm.get(['endtime'])!.value, DATE_TIME_FORMAT) : undefined,
      enddate: this.editForm.get(['enddate'])!.value,
      tblTaskId: this.editForm.get(['tblTaskId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventHsnx>>): void {
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

  trackById(index: number, item: ITaskHsnx): any {
    return item.id;
  }
}
