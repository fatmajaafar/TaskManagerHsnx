import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmployeeHsnx, EmployeeHsnx } from 'app/shared/model/employee-hsnx.model';
import { EmployeeHsnxService } from './employee-hsnx.service';
import { EmployeeHsnxComponent } from './employee-hsnx.component';
import { EmployeeHsnxDetailComponent } from './employee-hsnx-detail.component';
import { EmployeeHsnxUpdateComponent } from './employee-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class EmployeeHsnxResolve implements Resolve<IEmployeeHsnx> {
  constructor(private service: EmployeeHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employee: HttpResponse<EmployeeHsnx>) => {
          if (employee.body) {
            return of(employee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmployeeHsnx());
  }
}

export const employeeRoute: Routes = [
  {
    path: '',
    component: EmployeeHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.employee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EmployeeHsnxDetailComponent,
    resolve: {
      employee: EmployeeHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.employee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EmployeeHsnxUpdateComponent,
    resolve: {
      employee: EmployeeHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.employee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EmployeeHsnxUpdateComponent,
    resolve: {
      employee: EmployeeHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.employee.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
