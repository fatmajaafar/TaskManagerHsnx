import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ContainerComponent } from './container.component';

export const containerRoute: Routes = [
  {
    path: 'kanban',
    component: ContainerComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService]
  }
];
