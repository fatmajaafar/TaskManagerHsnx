import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJobHsnx, JobHsnx } from 'app/shared/model/job-hsnx.model';
import { JobHsnxService } from './job-hsnx.service';

@Component({
  selector: 'jhi-job-hsnx-update',
  templateUrl: './job-hsnx-update.component.html'
})
export class JobHsnxUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    jobTitle: [null, [Validators.required]]
  });

  constructor(protected jobService: JobHsnxService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);
    });
  }

  updateForm(job: IJobHsnx): void {
    this.editForm.patchValue({
      id: job.id,
      jobTitle: job.jobTitle
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJobHsnx {
    return {
      ...new JobHsnx(),
      id: this.editForm.get(['id'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobHsnx>>): void {
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
