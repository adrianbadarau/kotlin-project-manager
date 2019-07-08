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
import { MilestoneManageComponent } from './milestone-manage/milestone-manage.component';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { EditorModule } from 'primeng/editor';
import { CalendarModule } from 'primeng/calendar';
import { InputTextModule } from 'primeng/inputtext';
import { AutoCompleteModule } from 'primeng/autocomplete';

const ENTITY_STATES = [...milestoneRoute, ...milestonePopupRoute];

@NgModule({
  imports: [
    KotlinPmSharedModule,
    RouterModule.forChild(ENTITY_STATES),
    InputTextModule,
    CalendarModule,
    EditorModule,
    AutoCompleteModule,
    PanelModule,
    TableModule
  ],
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
export class KotlinPmMilestoneModule {}
