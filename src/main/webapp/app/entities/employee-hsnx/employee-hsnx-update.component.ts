import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEmployeeHsnx, EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from './employee-hsnx.service';
import { IJobHsnx } from 'app/shared/model/job-hsnx.model';
import { JobHsnxService } from 'app/entities/job-hsnx/job-hsnx.service';
import { IDepartmentHsnx } from 'app/shared/model/department-hsnx.model';
import { DepartmentHsnxService } from 'app/entities/department-hsnx/department-hsnx.service';
import { IBranchHsnx } from 'app/shared/model/branch-hsnx.model';
import { BranchHsnxService } from 'app/entities/branch-hsnx/branch-hsnx.service';

type SelectableEntity = IJobHsnx | IDepartmentHsnx | IBranchHsnx;

@Component({
  selector: 'jhi-employee-hsnx-update',
  templateUrl: './employee-hsnx-update.component.html'
})
export class EmployeeHsnxUpdateComponent implements OnInit {
  isSaving = false;
  jobs: IJobHsnx[] = [];
  departments: IDepartmentHsnx[] = [];
  branches: IBranchHsnx[] = [];
  employeehiredateDp: any;

  editForm = this.fb.group({
    id: [],
    employeename: [null, [Validators.required]],
    employeephone: [],
    employeefax: [],
    employeeaddress: [],
    employeeemail: [],
    employeehiredate: [],
    tblJobId: [],
    tblDepartmentId: [],
    tblBranchId: []
  });

  constructor(
    protected employeeService: EmployeeHsnxService,
    protected JobService: JobHsnxService,
    protected DepartmentService: DepartmentHsnxService,
    protected BranchService: BranchHsnxService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.JobService.query().subscribe((res: HttpResponse<IJobHsnx[]>) => (this.jobs = res.body || []));

      this.DepartmentService.query().subscribe((res: HttpResponse<IDepartmentHsnx[]>) => (this.departments = res.body || []));

      this.BranchService.query().subscribe((res: HttpResponse<IBranchHsnx[]>) => (this.branches = res.body || []));
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
      employeehiredate: employee.employeehiredate,
      tblJobId: employee.tblJobId,
      tblDepartmentId: employee.tblDepartmentId,
      tblBranchId: employee.tblBranchId
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
      employeehiredate: this.editForm.get(['employeehiredate'])!.value,
      tblJobId: this.editForm.get(['tblJobId'])!.value,
      tblDepartmentId: this.editForm.get(['tblDepartmentId'])!.value,
      tblBranchId: this.editForm.get(['tblBranchId'])!.value
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
