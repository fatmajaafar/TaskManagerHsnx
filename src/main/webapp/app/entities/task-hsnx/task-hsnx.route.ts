import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITaskHsnx, TaskHsnx } from 'app/shared/model/task-hsnx.model';
import { TaskHsnxService } from './task-hsnx.service';
import { TaskHsnxComponent } from './task-hsnx.component';
import { TaskHsnxDetailComponent } from './task-hsnx-detail.component';
import { TaskHsnxUpdateComponent } from './task-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class TaskHsnxResolve implements Resolve<ITaskHsnx> {
  constructor(private service: TaskHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((task: HttpResponse<TaskHsnx>) => {
          if (task.body) {
            return of(task.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaskHsnx());
  }
}

export const taskRoute: Routes = [
  {
    path: '',
    component: TaskHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.task.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TaskHsnxDetailComponent,
    resolve: {
      task: TaskHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.task.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TaskHsnxUpdateComponent,
    resolve: {
      task: TaskHsnxResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.task.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TaskHsnxUpdateComponent,
    resolve: {
      task: TaskHsnxResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.task.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
