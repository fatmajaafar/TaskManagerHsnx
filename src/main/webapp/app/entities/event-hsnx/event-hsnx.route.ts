import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEventHsnx, EventHsnx } from 'app/shared/model/event-hsnx.model';
import { EventHsnxService } from './event-hsnx.service';
import { EventHsnxComponent } from './event-hsnx.component';
import { EventHsnxDetailComponent } from './event-hsnx-detail.component';
import { EventHsnxUpdateComponent } from './event-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class EventHsnxResolve implements Resolve<IEventHsnx> {
  constructor(private service: EventHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((event: HttpResponse<EventHsnx>) => {
          if (event.body) {
            return of(event.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventHsnx());
  }
}

export const eventRoute: Routes = [
  {
    path: '',
    component: EventHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EventHsnxDetailComponent,
    resolve: {
      event: EventHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EventHsnxUpdateComponent,
    resolve: {
      event: EventHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EventHsnxUpdateComponent,
    resolve: {
      event: EventHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.event.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
