import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICountryHsnx, CountryHsnx } from 'app/shared/model/country-hsnx.model';
import { CountryHsnxService } from './country-hsnx.service';

@Component({
  selector: 'jhi-country-hsnx-update',
  templateUrl: './country-hsnx-update.component.html'
})
export class CountryHsnxUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    countryName: [null, [Validators.required]],
    countryCode: []
  });

  constructor(protected countryService: CountryHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);
    });
  }

  updateForm(country: ICountryHsnx): void {
    this.editForm.patchValue({
      id: country.id,
      countryName: country.countryName,
      countryCode: country.countryCode
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  private createFromForm(): ICountryHsnx {
    return {
      ...new CountryHsnx(),
      id: this.editForm.get(['id'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      countryCode: this.editForm.get(['countryCode'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountryHsnx>>): void {
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
