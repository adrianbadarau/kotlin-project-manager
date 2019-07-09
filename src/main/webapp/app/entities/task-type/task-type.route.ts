import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from './task-type.service';
import { TaskTypeComponent } from './task-type.component';
import { TaskTypeDetailComponent } from './task-type-detail.component';
import { TaskTypeUpdateComponent } from './task-type-update.component';
import { TaskTypeDeletePopupComponent } from './task-type-delete-dialog.component';
import { ITaskType } from 'app/shared/model/task-type.model';

@Injectable({ providedIn: 'root' })
export class TaskTypeResolve implements Resolve<ITaskType> {
  constructor(private service: TaskTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITaskType> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TaskType>) => response.ok),
        map((taskType: HttpResponse<TaskType>) => taskType.body)
      );
    }
    return of(new TaskType());
  }
}

export const taskTypeRoute: Routes = [
  {
    path: '',
    component: TaskTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TaskTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TaskTypeDetailComponent,
    resolve: {
      taskType: TaskTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TaskTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TaskTypeUpdateComponent,
    resolve: {
      taskType: TaskTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TaskTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TaskTypeUpdateComponent,
    resolve: {
      taskType: TaskTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TaskTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const taskTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TaskTypeDeletePopupComponent,
    resolve: {
      taskType: TaskTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TaskTypes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
