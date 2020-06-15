import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICountryHsnx, CountryHsnx } from 'app/shared/model/country-hsnx.model';
import { CountryHsnxService } from './country-hsnx.service';
import { CountryHsnxComponent } from './country-hsnx.component';
import { CountryHsnxDetailComponent } from './country-hsnx-detail.component';
import { CountryHsnxUpdateComponent } from './country-hsnx-update.component';

@Injectable({ providedIn: 'root' })
export class CountryHsnxResolve implements Resolve<ICountryHsnx> {
  constructor(private service: CountryHsnxService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountryHsnx> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((country: HttpResponse<CountryHsnx>) => {
          if (country.body) {
            return of(country.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountryHsnx());
  }
}

export const countryRoute: Routes = [
  {
    path: '',
    component: CountryHsnxComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskManagerHsnxApp.country.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CountryHsnxDetailComponent,
    resolve: {
      country: CountryHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.country.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CountryHsnxUpdateComponent,
    resolve: {
      country: CountryHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.country.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CountryHsnxUpdateComponent,
    resolve: {
      country: CountryHsnxResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskManagerHsnxApp.country.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
