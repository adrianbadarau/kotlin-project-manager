import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  MilestoneComponent,
  MilestoneDetailComponent,
  MilestoneUpdateComponent,
  MilestoneDeletePopupComponent,
  MilestoneDeleteDialogComponent,
  milestoneRoute,
  milestonePopupRoute
} from './';
import { MilestoneManageComponent } from 'app/entities/milestone/milestone-manage/milestone-manage.component';
import { AutoCompleteModule, CalendarModule, InputTextModule } from 'primeng/primeng';

const ENTITY_STATES = [...milestoneRoute, ...milestonePopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES), InputTextModule, CalendarModule, AutoCompleteModule],
  declarations: [
    MilestoneComponent,
    MilestoneDetailComponent,
    MilestoneUpdateComponent,
    MilestoneDeleteDialogComponent,
    MilestoneDeletePopupComponent,
    MilestoneManageComponent
  ],
  entryComponents: [MilestoneComponent, MilestoneUpdateComponent, MilestoneDeleteDialogComponent, MilestoneDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppMilestoneModule {}
