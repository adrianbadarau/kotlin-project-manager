import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Milestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { MilestoneComponent } from './milestone.component';
import { MilestoneDetailComponent } from './milestone-detail.component';
import { MilestoneUpdateComponent } from './milestone-update.component';
import { MilestoneDeletePopupComponent } from './milestone-delete-dialog.component';
import { IMilestone } from 'app/shared/model/milestone.model';

@Injectable({ providedIn: 'root' })
export class MilestoneResolve implements Resolve<IMilestone> {
  constructor(private service: MilestoneService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMilestone> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Milestone>) => response.ok),
        map((milestone: HttpResponse<Milestone>) => milestone.body)
      );
    }
    return of(new Milestone());
  }
}

export const milestoneRoute: Routes = [
  {
    path: '',
    component: MilestoneComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Milestones'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MilestoneDetailComponent,
    resolve: {
      milestone: MilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Milestones'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MilestoneUpdateComponent,
    resolve: {
      milestone: MilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Milestones'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MilestoneUpdateComponent,
    resolve: {
      milestone: MilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Milestones'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const milestonePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MilestoneDeletePopupComponent,
    resolve: {
      milestone: MilestoneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Milestones'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
