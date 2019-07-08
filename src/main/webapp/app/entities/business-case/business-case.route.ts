import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from './business-case.service';
import { BusinessCaseComponent } from './business-case.component';
import { BusinessCaseDetailComponent } from './business-case-detail.component';
import { BusinessCaseUpdateComponent } from './business-case-update.component';
import { BusinessCaseDeletePopupComponent } from './business-case-delete-dialog.component';
import { IBusinessCase } from 'app/shared/model/business-case.model';

@Injectable({ providedIn: 'root' })
export class BusinessCaseResolve implements Resolve<IBusinessCase> {
  constructor(private service: BusinessCaseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBusinessCase> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BusinessCase>) => response.ok),
        map((businessCase: HttpResponse<BusinessCase>) => businessCase.body)
      );
    }
    return of(new BusinessCase());
  }
}

export const businessCaseRoute: Routes = [
  {
    path: '',
    component: BusinessCaseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BusinessCases'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BusinessCaseDetailComponent,
    resolve: {
      businessCase: BusinessCaseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BusinessCases'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BusinessCaseUpdateComponent,
    resolve: {
      businessCase: BusinessCaseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BusinessCases'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BusinessCaseUpdateComponent,
    resolve: {
      businessCase: BusinessCaseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BusinessCases'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const businessCasePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BusinessCaseDeletePopupComponent,
    resolve: {
      businessCase: BusinessCaseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BusinessCases'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
