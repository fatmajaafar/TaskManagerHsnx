import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventHsnx } from 'app/shared/model/event-hsnx.model';

@Component({
  selector: 'jhi-event-hsnx-detail',
  templateUrl: './event-hsnx-detail.component.html'
})
export class EventHsnxDetailComponent implements OnInit {
  event: IEventHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => (this.event = event));
  }

  previousState(): void {
    window.history.back();
  }
}
