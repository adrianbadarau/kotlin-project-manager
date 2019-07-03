import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KotlinPmSharedModule } from 'app/shared';
import {
  MilestoneComponent,
  MilestoneDetailComponent,
  MilestoneUpdateComponent,
  MilestoneDeletePopupComponent,
  MilestoneDeleteDialogComponent,
  milestoneRoute,
  milestonePopupRoute
} from './';

const ENTITY_STATES = [...milestoneRoute, ...milestonePopupRoute];

@NgModule({
  imports: [KotlinPmSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MilestoneComponent,
    MilestoneDetailComponent,
    MilestoneUpdateComponent,
    MilestoneDeleteDialogComponent,
    MilestoneDeletePopupComponent
  ],
  entryComponents: [MilestoneComponent, MilestoneUpdateComponent, MilestoneDeleteDialogComponent, MilestoneDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KotlinPmMilestoneModule {}
