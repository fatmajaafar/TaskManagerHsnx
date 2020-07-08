import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

import { TaskSchedulerComponent } from './task-scheduler.component';

export const taskSchedulerRoute: Routes = [
  {
    path: 'task-scheduler',
    component: TaskSchedulerComponent,
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
