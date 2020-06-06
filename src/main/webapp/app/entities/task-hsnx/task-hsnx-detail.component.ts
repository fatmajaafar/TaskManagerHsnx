import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';

@Component({
  selector: 'jhi-task-hsnx-detail',
  templateUrl: './task-hsnx-detail.component.html'
})
export class TaskHsnxDetailComponent implements OnInit {
  task: ITaskHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => (this.task = task));
  }

  previousState(): void {
    window.history.back();
  }
}
