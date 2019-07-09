import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Attachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';
import { AttachmentComponent } from './attachment.component';
import { AttachmentDetailComponent } from './attachment-detail.component';
import { AttachmentUpdateComponent } from './attachment-update.component';
import { AttachmentDeletePopupComponent } from './attachment-delete-dialog.component';
import { IAttachment } from 'app/shared/model/attachment.model';

@Injectable({ providedIn: 'root' })
export class AttachmentResolve implements Resolve<IAttachment> {
  constructor(private service: AttachmentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAttachment> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Attachment>) => response.ok),
        map((attachment: HttpResponse<Attachment>) => attachment.body)
      );
    }
    return of(new Attachment());
  }
}

export const attachmentRoute: Routes = [
  {
    path: '',
    component: AttachmentComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attachments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AttachmentDetailComponent,
    resolve: {
      attachment: AttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attachments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AttachmentUpdateComponent,
    resolve: {
      attachment: AttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attachments'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AttachmentUpdateComponent,
    resolve: {
      attachment: AttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attachments'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const attachmentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AttachmentDeletePopupComponent,
    resolve: {
      attachment: AttachmentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attachments'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
