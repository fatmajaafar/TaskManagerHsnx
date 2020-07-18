import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJobHsnx } from 'app/shared/model/job-hsnx.model';
import { JobHsnxService } from './job-hsnx.service';

@Component({
  templateUrl: './job-hsnx-delete-dialog.component.html'
})
export class JobHsnxDeleteDialogComponent {
  job?: IJobHsnx;

  constructor(protected jobService: JobHsnxService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jobListModification');
      this.activeModal.close();
    });
  }
}
