import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ProfileService } from './profile.service';
import { ProfileComponent } from './profile.component';
import { Profile, IProfile } from 'app/shared/model/profile.model';

@Injectable({ providedIn: 'root' })
export class taskManagementHsnxResolve implements Resolve<IProfile> {
  constructor(private service: ProfileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfile> | Observable<never> {
    return of(new Profile());
  }
}

export const profileRoute: Routes = [
  {
    path: '',
    component: ProfileComponent,
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
