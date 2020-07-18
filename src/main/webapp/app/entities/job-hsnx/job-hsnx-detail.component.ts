import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobHsnx } from 'app/shared/model/job-hsnx.model';

@Component({
  selector: 'jhi-job-hsnx-detail',
  templateUrl: './job-hsnx-detail.component.html'
})
export class JobHsnxDetailComponent implements OnInit {
  job: IJobHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => (this.job = job));
  }

  previousState(): void {
    window.history.back();
  }
}
