import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  ProjectUpdateComponent,
  ProjectUpdateDetailComponent,
  ProjectUpdateUpdateComponent,
  ProjectUpdateDeletePopupComponent,
  ProjectUpdateDeleteDialogComponent,
  projectUpdateRoute,
  projectUpdatePopupRoute
} from './';

const ENTITY_STATES = [...projectUpdateRoute, ...projectUpdatePopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProjectUpdateComponent,
    ProjectUpdateDetailComponent,
    ProjectUpdateUpdateComponent,
    ProjectUpdateDeleteDialogComponent,
    ProjectUpdateDeletePopupComponent
  ],
  entryComponents: [
    ProjectUpdateComponent,
    ProjectUpdateUpdateComponent,
    ProjectUpdateDeleteDialogComponent,
    ProjectUpdateDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppProjectUpdateModule {}
