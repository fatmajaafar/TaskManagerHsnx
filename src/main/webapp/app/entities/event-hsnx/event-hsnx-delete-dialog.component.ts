import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEventHsnx } from 'app/shared/model/event-hsnx.model';
import { EventHsnxService } from './event-hsnx.service';

@Component({
  templateUrl: './event-hsnx-delete-dialog.component.html'
})
export class EventHsnxDeleteDialogComponent {
  event?: IEventHsnx;

  constructor(protected eventService: EventHsnxService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventService.delete(id).subscribe(() => {
      this.eventManager.broadcast('eventListModification');
      this.activeModal.close();
    });
  }
}
