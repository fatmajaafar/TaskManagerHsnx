import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountryHsnx } from 'app/shared/model/country-hsnx.model';

@Component({
  selector: 'jhi-country-hsnx-detail',
  templateUrl: './country-hsnx-detail.component.html'
})
export class CountryHsnxDetailComponent implements OnInit {
  country: ICountryHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => (this.country = country));
  }

  previousState(): void {
    window.history.back();
  }
}
