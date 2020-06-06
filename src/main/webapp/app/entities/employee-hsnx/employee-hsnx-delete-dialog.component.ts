import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from './employee-hsnx.service';

@Component({
  templateUrl: './employee-hsnx-delete-dialog.component.html'
})
export class EmployeeHsnxDeleteDialogComponent {
  employee?: IEmployeeHsnx;

  constructor(
    protected employeeService: EmployeeHsnxService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeeListModification');
      this.activeModal.close();
    });
  }
}
