import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDepartmentHsnx, DepartmentHsnx } from 'app/shared/model/department-hsnx.model';
import { DepartmentHsnxService } from './department-hsnx.service';
import { ICountryHsnx } from 'app/shared/model/country-hsnx.model';
import { CountryHsnxService } from '../country-hsnx/country-hsnx.service';

@Component({
  selector: 'jhi-department-hsnx-update',
  templateUrl: './department-hsnx-update.component.html'
})
export class DepartmentHsnxUpdateComponent implements OnInit {
  isSaving = false;
  countries: ICountryHsnx[] = [];
  editForm = this.fb.group({
    id: [],
    deptName: [null, [Validators.required]],
    deptNote: [],
    tblCountryId: []
  });

  constructor(
    protected departmentService: DepartmentHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected CountryService: CountryHsnxService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.updateForm(department);
    });

    this.CountryService.query().subscribe((res: HttpResponse<ICountryHsnx[]>) => (this.countries = res.body || []));
  }

  updateForm(department: IDepartmentHsnx): void {
    this.editForm.patchValue({
      id: department.id,
      deptName: department.deptName,
      deptNote: department.deptNote,
      tblCountryId: department.tblCountryId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.createFromForm();
    if (department.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  private createFromForm(): IDepartmentHsnx {
    return {
      ...new DepartmentHsnx(),
      id: this.editForm.get(['id'])!.value,
      deptName: this.editForm.get(['deptName'])!.value,
      deptNote: this.editForm.get(['deptNote'])!.value,
      tblCountryId: this.editForm.get(['tblCountryId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartmentHsnx>>): void {
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
