import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { LoginModalComponent } from 'app/shared/login/login.component';

@Injectable({ providedIn: 'root' })
export class LoginModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): NgbModalRef {
    this.isOpen = true;
    const modalRef = this.modalService.open(LoginModalComponent, { windowClass: 'hugeModal', keyboard: false });

    return modalRef;
  }
}
