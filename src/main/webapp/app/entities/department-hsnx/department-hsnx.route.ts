import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDepartmentHsnx, DepartmentHsnx } from 'app/shared/model/department-hsnx.model';
import { DepartmentHsnxService } from './department-hsnx.service';
import { DepartmentHsnxComponent } from './department-hsnx.component';
import { DepartmentHsnxDetailComponent } from './department-hsnx-detail.component';
import { DepartmentHsnxUpdateComponent } from './department-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class DepartmentHsnxResolve implements Resolve<IDepartmentHsnx> {
  constructor(private service: DepartmentHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepartmentHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((department: HttpResponse<DepartmentHsnx>) => {
          if (department.body) {
            return of(department.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepartmentHsnx());
  }
}

export const departmentRoute: Routes = [
  {
    path: '',
    component: DepartmentHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.department.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DepartmentHsnxDetailComponent,
    resolve: {
      department: DepartmentHsnxResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.department.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DepartmentHsnxUpdateComponent,
    resolve: {
      department: DepartmentHsnxResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.department.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DepartmentHsnxUpdateComponent,
    resolve: {
      department: DepartmentHsnxResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'taskManagerHsnxApp.department.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
