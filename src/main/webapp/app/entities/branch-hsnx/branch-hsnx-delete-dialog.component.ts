import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBranchHsnx } from 'app/shared/model/branch-hsnx.model';
import { BranchHsnxService } from './branch-hsnx.service';

@Component({
  templateUrl: './branch-hsnx-delete-dialog.component.html'
})
export class BranchHsnxDeleteDialogComponent {
  branch?: IBranchHsnx;

  constructor(protected branchService: BranchHsnxService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.branchService.delete(id).subscribe(() => {
      this.eventManager.broadcast('branchListModification');
      this.activeModal.close();
    });
  }
}
