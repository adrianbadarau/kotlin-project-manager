import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChangeHistory } from 'app/shared/model/change-history.model';
import { ChangeHistoryService } from './change-history.service';
import { ChangeHistoryComponent } from './change-history.component';
import { ChangeHistoryDetailComponent } from './change-history-detail.component';
import { ChangeHistoryUpdateComponent } from './change-history-update.component';
import { ChangeHistoryDeletePopupComponent } from './change-history-delete-dialog.component';
import { IChangeHistory } from 'app/shared/model/change-history.model';

@Injectable({ providedIn: 'root' })
export class ChangeHistoryResolve implements Resolve<IChangeHistory> {
  constructor(private service: ChangeHistoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChangeHistory> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChangeHistory>) => response.ok),
        map((changeHistory: HttpResponse<ChangeHistory>) => changeHistory.body)
      );
    }
    return of(new ChangeHistory());
  }
}

export const changeHistoryRoute: Routes = [
  {
    path: '',
    component: ChangeHistoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChangeHistoryDetailComponent,
    resolve: {
      changeHistory: ChangeHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChangeHistoryUpdateComponent,
    resolve: {
      changeHistory: ChangeHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeHistories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChangeHistoryUpdateComponent,
    resolve: {
      changeHistory: ChangeHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeHistories'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const changeHistoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChangeHistoryDeletePopupComponent,
    resolve: {
      changeHistory: ChangeHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ChangeHistories'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
