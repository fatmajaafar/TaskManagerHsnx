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

@Component({
  selector: 'jhi-event-hsnx-update',
  templateUrl: './event-hsnx-update.component.html'
})
export class EventHsnxUpdateComponent implements OnInit {
  isSaving = false;
  startdateDp: any;
  enddateDp: any;

  editForm = this.fb.group({
    id: [],
    Description: [null, [Validators.required]],
    starttime: [],
    startdate: [],
    endtime: [],
    enddate: []
  });

  constructor(protected eventService: EventHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      if (!event.id) {
        const today = moment().startOf('day');
        event.starttime = today;
        event.endtime = today;
      }

      this.updateForm(event);
    });
  }

  updateForm(event: IEventHsnx): void {
    this.editForm.patchValue({
      id: event.id,
      Description: event.Description,
      starttime: event.starttime ? event.starttime.format(DATE_TIME_FORMAT) : null,
      startdate: event.startdate,
      endtime: event.endtime ? event.endtime.format(DATE_TIME_FORMAT) : null,
      enddate: event.enddate
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
      Description: this.editForm.get(['Description'])!.value,
      starttime: this.editForm.get(['starttime'])!.value ? moment(this.editForm.get(['starttime'])!.value, DATE_TIME_FORMAT) : undefined,
      startdate: this.editForm.get(['startdate'])!.value,
      endtime: this.editForm.get(['endtime'])!.value ? moment(this.editForm.get(['endtime'])!.value, DATE_TIME_FORMAT) : undefined,
      enddate: this.editForm.get(['enddate'])!.value
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
}
