import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Priority } from 'app/shared/model/priority.model';
import { PriorityService } from './priority.service';
import { PriorityComponent } from './priority.component';
import { PriorityDetailComponent } from './priority-detail.component';
import { PriorityUpdateComponent } from './priority-update.component';
import { PriorityDeletePopupComponent } from './priority-delete-dialog.component';
import { IPriority } from 'app/shared/model/priority.model';

@Injectable({ providedIn: 'root' })
export class PriorityResolve implements Resolve<IPriority> {
  constructor(private service: PriorityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPriority> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Priority>) => response.ok),
        map((priority: HttpResponse<Priority>) => priority.body)
      );
    }
    return of(new Priority());
  }
}

export const priorityRoute: Routes = [
  {
    path: '',
    component: PriorityComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Priorities'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PriorityDetailComponent,
    resolve: {
      priority: PriorityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Priorities'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Priorities'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Priorities'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const priorityPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PriorityDeletePopupComponent,
    resolve: {
      priority: PriorityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Priorities'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
