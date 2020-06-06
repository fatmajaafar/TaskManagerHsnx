import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEmployeeHsnx, EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from './employee-hsnx.service';

@Component({
  selector: 'jhi-employee-hsnx-update',
  templateUrl: './employee-hsnx-update.component.html'
})
export class EmployeeHsnxUpdateComponent implements OnInit {
  isSaving = false;
  branchHiredateDp: any;

  editForm = this.fb.group({
    id: [],
    branchName: [null, [Validators.required]],
    branchPhone: [],
    branchFax: [],
    branchAddress: [],
    branchEmail: [],
    branchHiredate: []
  });

  constructor(protected employeeService: EmployeeHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);
    });
  }

  updateForm(employee: IEmployeeHsnx): void {
    this.editForm.patchValue({
      id: employee.id,
      branchName: employee.branchName,
      branchPhone: employee.branchPhone,
      branchFax: employee.branchFax,
      branchAddress: employee.branchAddress,
      branchEmail: employee.branchEmail,
      branchHiredate: employee.branchHiredate
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  private createFromForm(): IEmployeeHsnx {
    return {
      ...new EmployeeHsnx(),
      id: this.editForm.get(['id'])!.value,
      branchName: this.editForm.get(['branchName'])!.value,
      branchPhone: this.editForm.get(['branchPhone'])!.value,
      branchFax: this.editForm.get(['branchFax'])!.value,
      branchAddress: this.editForm.get(['branchAddress'])!.value,
      branchEmail: this.editForm.get(['branchEmail'])!.value,
      branchHiredate: this.editForm.get(['branchHiredate'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeHsnx>>): void {
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
