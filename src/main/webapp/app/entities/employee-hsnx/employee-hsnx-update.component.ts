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
  employeehiredateDp: any;

  editForm = this.fb.group({
    id: [],
    employeename: [null, [Validators.required]],
    employeephone: [],
    employeefax: [],
    employeeaddress: [],
    employeeemail: [],
    employeehiredate: []
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
      employeename: employee.employeename,
      employeephone: employee.employeephone,
      employeefax: employee.employeefax,
      employeeaddress: employee.employeeaddress,
      employeeemail: employee.employeeemail,
      employeehiredate: employee.employeehiredate
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
      employeename: this.editForm.get(['employeename'])!.value,
      employeephone: this.editForm.get(['employeephone'])!.value,
      employeefax: this.editForm.get(['employeefax'])!.value,
      employeeaddress: this.editForm.get(['employeeaddress'])!.value,
      employeeemail: this.editForm.get(['employeeemail'])!.value,
      employeehiredate: this.editForm.get(['employeehiredate'])!.value
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
