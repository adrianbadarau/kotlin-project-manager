import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProjectUpdate } from 'app/shared/model/project-update.model';
import { ProjectUpdateService } from './project-update.service';
import { ProjectUpdateComponent } from './project-update.component';
import { ProjectUpdateDetailComponent } from './project-update-detail.component';
import { ProjectUpdateUpdateComponent } from './project-update-update.component';
import { ProjectUpdateDeletePopupComponent } from './project-update-delete-dialog.component';
import { IProjectUpdate } from 'app/shared/model/project-update.model';

@Injectable({ providedIn: 'root' })
export class ProjectUpdateResolve implements Resolve<IProjectUpdate> {
  constructor(private service: ProjectUpdateService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProjectUpdate> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ProjectUpdate>) => response.ok),
        map((projectUpdate: HttpResponse<ProjectUpdate>) => projectUpdate.body)
      );
    }
    return of(new ProjectUpdate());
  }
}

export const projectUpdateRoute: Routes = [
  {
    path: '',
    component: ProjectUpdateComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProjectUpdates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProjectUpdateDetailComponent,
    resolve: {
      projectUpdate: ProjectUpdateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProjectUpdates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProjectUpdateUpdateComponent,
    resolve: {
      projectUpdate: ProjectUpdateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProjectUpdates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProjectUpdateUpdateComponent,
    resolve: {
      projectUpdate: ProjectUpdateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProjectUpdates'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const projectUpdatePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProjectUpdateDeletePopupComponent,
    resolve: {
      projectUpdate: ProjectUpdateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProjectUpdates'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
