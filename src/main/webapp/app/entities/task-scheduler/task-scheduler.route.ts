import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

import { TaskSchedulerComponent } from './task-scheduler.component';
import { ITaskScheduler, TaskScheduler } from 'app/shared/model/task-scheduler.model';

export const taskSchedulerRoute: Routes = [
  {
    path: '',
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
