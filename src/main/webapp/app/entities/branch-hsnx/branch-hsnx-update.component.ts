import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBranchHsnx, BranchHsnx } from 'app/shared/model/branch-hsnx.model';
import { BranchHsnxService } from './branch-hsnx.service';

@Component({
  selector: 'jhi-branch-hsnx-update',
  templateUrl: './branch-hsnx-update.component.html'
})
export class BranchHsnxUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    branchName: [null, [Validators.required]],
    branchPhone: [],
    branchFax: [],
    branchAddress: [],
    branchEmail: [],
    branchBanner: [],
    branchDefaultExercice: []
  });

  constructor(protected branchService: BranchHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => {
      this.updateForm(branch);
    });
  }

  updateForm(branch: IBranchHsnx): void {
    this.editForm.patchValue({
      id: branch.id,
      branchName: branch.branchName,
      branchPhone: branch.branchPhone,
      branchFax: branch.branchFax,
      branchAddress: branch.branchAddress,
      branchEmail: branch.branchEmail,
      branchBanner: branch.branchBanner,
      branchDefaultExercice: branch.branchDefaultExercice
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const branch = this.createFromForm();
    if (branch.id !== undefined) {
      this.subscribeToSaveResponse(this.branchService.update(branch));
    } else {
      this.subscribeToSaveResponse(this.branchService.create(branch));
    }
  }

  private createFromForm(): IBranchHsnx {
    return {
      ...new BranchHsnx(),
      id: this.editForm.get(['id'])!.value,
      branchName: this.editForm.get(['branchName'])!.value,
      branchPhone: this.editForm.get(['branchPhone'])!.value,
      branchFax: this.editForm.get(['branchFax'])!.value,
      branchAddress: this.editForm.get(['branchAddress'])!.value,
      branchEmail: this.editForm.get(['branchEmail'])!.value,
      branchBanner: this.editForm.get(['branchBanner'])!.value,
      branchDefaultExercice: this.editForm.get(['branchDefaultExercice'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBranchHsnx>>): void {
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
