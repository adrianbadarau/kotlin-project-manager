import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BenefitType } from 'app/shared/model/benefit-type.model';
import { BenefitTypeService } from './benefit-type.service';
import { BenefitTypeComponent } from './benefit-type.component';
import { BenefitTypeDetailComponent } from './benefit-type-detail.component';
import { BenefitTypeUpdateComponent } from './benefit-type-update.component';
import { BenefitTypeDeletePopupComponent } from './benefit-type-delete-dialog.component';
import { IBenefitType } from 'app/shared/model/benefit-type.model';

@Injectable({ providedIn: 'root' })
export class BenefitTypeResolve implements Resolve<IBenefitType> {
  constructor(private service: BenefitTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBenefitType> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BenefitType>) => response.ok),
        map((benefitType: HttpResponse<BenefitType>) => benefitType.body)
      );
    }
    return of(new BenefitType());
  }
}

export const benefitTypeRoute: Routes = [
  {
    path: '',
    component: BenefitTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BenefitTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BenefitTypeDetailComponent,
    resolve: {
      benefitType: BenefitTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BenefitTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BenefitTypeUpdateComponent,
    resolve: {
      benefitType: BenefitTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BenefitTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BenefitTypeUpdateComponent,
    resolve: {
      benefitType: BenefitTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BenefitTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const benefitTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BenefitTypeDeletePopupComponent,
    resolve: {
      benefitType: BenefitTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BenefitTypes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
