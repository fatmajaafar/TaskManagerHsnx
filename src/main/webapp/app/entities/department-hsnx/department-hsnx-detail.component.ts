import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepartmentHsnx } from 'app/shared/model/department-hsnx.model';

@Component({
  selector: 'jhi-department-hsnx-detail',
  templateUrl: './department-hsnx-detail.component.html'
})
export class DepartmentHsnxDetailComponent implements OnInit {
  department: IDepartmentHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => (this.department = department));
  }

  previousState(): void {
    window.history.back();
  }
}
