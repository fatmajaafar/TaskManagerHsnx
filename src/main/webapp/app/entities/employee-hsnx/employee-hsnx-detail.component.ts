import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeHsnx } from 'app/shared/model/employee-hsnx.model';

@Component({
  selector: 'jhi-employee-hsnx-detail',
  templateUrl: './employee-hsnx-detail.component.html'
})
export class EmployeeHsnxDetailComponent implements OnInit {
  employee: IEmployeeHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => (this.employee = employee));
  }

  previousState(): void {
    window.history.back();
  }
}
