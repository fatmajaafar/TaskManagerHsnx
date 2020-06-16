import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotificationHsnx } from 'app/shared/model/notification-hsnx.model';

@Component({
  selector: 'jhi-notification-hsnx-detail',
  templateUrl: './notification-hsnx-detail.component.html'
})
export class NotificationHsnxDetailComponent implements OnInit {
  notification: INotificationHsnx | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => (this.notification = notification));
  }

  previousState(): void {
    window.history.back();
  }
}
