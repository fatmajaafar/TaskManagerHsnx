import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from './notification-hsnx.service';

@Component({
  templateUrl: './notification-hsnx-delete-dialog.component.html'
})
export class NotificationHsnxDeleteDialogComponent {
  notification?: INotificationHsnx;

  constructor(
    protected notificationService: NotificationHsnxService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('notificationListModification');
      this.activeModal.close();
    });
  }
}
