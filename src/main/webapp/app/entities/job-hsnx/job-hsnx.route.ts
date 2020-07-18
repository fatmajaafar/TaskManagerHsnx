import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJobHsnx, JobHsnx } from 'app/shared/model/job-hsnx.model';
import { JobHsnxService } from './job-hsnx.service';
import { JobHsnxComponent } from './job-hsnx.component';
import { JobHsnxDetailComponent } from './job-hsnx-detail.component';
import { JobHsnxUpdateComponent } from './job-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class JobHsnxResolve implements Resolve<IJobHsnx> {
  constructor(private service: JobHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((job: HttpResponse<JobHsnx>) => {
          if (job.body) {
            return of(job.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobHsnx());
  }
}

export const jobRoute: Routes = [
  {
    path: '',
    component: JobHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.job.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JobHsnxDetailComponent,
    resolve: {
      job: JobHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.job.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JobHsnxUpdateComponent,
    resolve: {
      job: JobHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.job.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JobHsnxUpdateComponent,
    resolve: {
      job: JobHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.job.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
