import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICountryHsnx } from 'app/shared/model/country-hsnx.model';
import { CountryHsnxService } from './country-hsnx.service';

@Component({
  templateUrl: './country-hsnx-delete-dialog.component.html'
})
export class CountryHsnxDeleteDialogComponent {
  country?: ICountryHsnx;

  constructor(protected countryService: CountryHsnxService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('countryListModification');
      this.activeModal.close();
    });
  }
}
