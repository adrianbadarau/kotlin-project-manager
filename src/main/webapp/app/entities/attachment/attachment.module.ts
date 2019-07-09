import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  AttachmentComponent,
  AttachmentDetailComponent,
  AttachmentUpdateComponent,
  AttachmentDeletePopupComponent,
  AttachmentDeleteDialogComponent,
  attachmentRoute,
  attachmentPopupRoute
} from './';

const ENTITY_STATES = [...attachmentRoute, ...attachmentPopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AttachmentComponent,
    AttachmentDetailComponent,
    AttachmentUpdateComponent,
    AttachmentDeleteDialogComponent,
    AttachmentDeletePopupComponent
  ],
  entryComponents: [AttachmentComponent, AttachmentUpdateComponent, AttachmentDeleteDialogComponent, AttachmentDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppAttachmentModule {}
