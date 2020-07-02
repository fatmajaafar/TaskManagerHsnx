import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

import { ITaskmanagementHsnx, TaskmanagementHsnx } from 'app/shared/model/taskmanagement-hsnx.model';
import { TaskmanagementHsnxComponent } from './taskmanagement-hsnx.component';

@Injectable({ providedIn: 'root' })
export class taskManagementHsnxResolve implements Resolve<ITaskmanagementHsnx> {
  constructor(private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaskmanagementHsnx> | Observable<never> {
    return of(new TaskmanagementHsnx());
  }
}

export const taskManagementRoute: Routes = [
  {
    path: '',
    component: TaskmanagementHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.task.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
