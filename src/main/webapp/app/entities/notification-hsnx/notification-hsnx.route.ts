import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INotificationHsnx, NotificationHsnx } from 'app/shared/model/notification-hsnx.model';
import { NotificationHsnxService } from './notification-hsnx.service';
import { NotificationHsnxComponent } from './notification-hsnx.component';
import { NotificationHsnxDetailComponent } from './notification-hsnx-detail.component';
import { NotificationHsnxUpdateComponent } from './notification-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class NotificationHsnxResolve implements Resolve<INotificationHsnx> {
  constructor(private service: NotificationHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotificationHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((notification: HttpResponse<NotificationHsnx>) => {
          if (notification.body) {
            return of(notification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NotificationHsnx());
  }
}

export const notificationRoute: Routes = [
  {
    path: '',
    component: NotificationHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.notification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NotificationHsnxDetailComponent,
    resolve: {
      notification: NotificationHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.notification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NotificationHsnxUpdateComponent,
    resolve: {
      notification: NotificationHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.notification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NotificationHsnxUpdateComponent,
    resolve: {
      notification: NotificationHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.notification.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
