import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskHsnx } from 'app/shared/model/task-hsnx.model';
import { ButtonComponent } from '@syncfusion/ej2-angular-buttons';
@Component({
  selector: 'jhi-task-hsnx-detail',
  templateUrl: './task-hsnx-detail.component.html'
})
export class TaskHsnxDetailComponent implements OnInit {
  @ViewChild('toggleBtn', { static: true })
  public toggleBtn!: ButtonComponent;

  task: ITaskHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => (this.task = task));
  }

  previousState(): void {
    window.history.back();
  }
}
