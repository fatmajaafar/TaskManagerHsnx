import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDepartmentHsnx } from 'app/shared/model/department-hsnx.model';
import { DepartmentHsnxService } from './department-hsnx.service';

@Component({
  templateUrl: './department-hsnx-delete-dialog.component.html'
})
export class DepartmentHsnxDeleteDialogComponent {
  department?: IDepartmentHsnx;

  constructor(
    protected departmentService: DepartmentHsnxService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departmentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('departmentListModification');
      this.activeModal.close();
    });
  }
}
