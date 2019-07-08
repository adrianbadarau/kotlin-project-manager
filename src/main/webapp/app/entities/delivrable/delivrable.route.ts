import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Delivrable } from 'app/shared/model/delivrable.model';
import { DelivrableService } from './delivrable.service';
import { DelivrableComponent } from './delivrable.component';
import { DelivrableDetailComponent } from './delivrable-detail.component';
import { DelivrableUpdateComponent } from './delivrable-update.component';
import { DelivrableDeletePopupComponent } from './delivrable-delete-dialog.component';
import { IDelivrable } from 'app/shared/model/delivrable.model';

@Injectable({ providedIn: 'root' })
export class DelivrableResolve implements Resolve<IDelivrable> {
  constructor(private service: DelivrableService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDelivrable> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Delivrable>) => response.ok),
        map((delivrable: HttpResponse<Delivrable>) => delivrable.body)
      );
    }
    return of(new Delivrable());
  }
}

export const delivrableRoute: Routes = [
  {
    path: '',
    component: DelivrableComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Delivrables'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DelivrableDetailComponent,
    resolve: {
      delivrable: DelivrableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Delivrables'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DelivrableUpdateComponent,
    resolve: {
      delivrable: DelivrableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Delivrables'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DelivrableUpdateComponent,
    resolve: {
      delivrable: DelivrableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Delivrables'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const delivrablePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DelivrableDeletePopupComponent,
    resolve: {
      delivrable: DelivrableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Delivrables'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
