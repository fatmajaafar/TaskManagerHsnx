import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBranchHsnx, BranchHsnx } from 'app/shared/model/branch-hsnx.model';
import { BranchHsnxService } from './branch-hsnx.service';
import { BranchHsnxComponent } from './branch-hsnx.component';
import { BranchHsnxDetailComponent } from './branch-hsnx-detail.component';
import { BranchHsnxUpdateComponent } from './branch-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class BranchHsnxResolve implements Resolve<IBranchHsnx> {
  constructor(private service: BranchHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBranchHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((branch: HttpResponse<BranchHsnx>) => {
          if (branch.body) {
            return of(branch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BranchHsnx());
  }
}

export const branchRoute: Routes = [
  {
    path: '',
    component: BranchHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.branch.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BranchHsnxDetailComponent,
    resolve: {
      branch: BranchHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.branch.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BranchHsnxUpdateComponent,
    resolve: {
      branch: BranchHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.branch.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BranchHsnxUpdateComponent,
    resolve: {
      branch: BranchHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.branch.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
