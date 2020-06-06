import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from './task-hsnx.service';

@Component({
  templateUrl: './task-hsnx-delete-dialog.component.html'
})
export class TaskHsnxDeleteDialogComponent {
  task?: ITaskHsnx;

  constructor(protected taskService: TaskHsnxService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taskListModification');
      this.activeModal.close();
    });
  }
}
