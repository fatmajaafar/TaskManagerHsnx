import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { INotificationHsnx, NotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from './notification-hsnx.service';

@Component({
  selector: 'jhi-notification-hsnx-update',
  templateUrl: './notification-hsnx-update.component.html'
})
export class NotificationHsnxUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    message: [null, [Validators.required]],
    handled: [],
    notifFrom: [],
    notifto: []
  });

  constructor(protected notificationService: NotificationHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      this.updateForm(notification);
    });
  }

  updateForm(notification: INotificationHsnx): void {
    this.editForm.patchValue({
      id: notification.id,
      message: notification.message,
      handled: notification.handled,
      notifFrom: notification.notifFrom,
      notifto: notification.notifto
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  private createFromForm(): INotificationHsnx {
    return {
      ...new NotificationHsnx(),
      id: this.editForm.get(['id'])!.value,
      message: this.editForm.get(['message'])!.value,
      handled: this.editForm.get(['handled'])!.value,
      notifFrom: this.editForm.get(['notifFrom'])!.value,
      notifto: this.editForm.get(['notifto'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationHsnx>>): void {
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
