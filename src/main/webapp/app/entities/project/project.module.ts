import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  ProjectComponent,
  ProjectDetailComponent,
  ProjectUpdateComponent,
  ProjectDeletePopupComponent,
  ProjectDeleteDialogComponent,
  projectRoute,
  projectPopupRoute
} from './';
import { ProjectManageComponent } from 'app/entities/project/manage/manage.component';
import { AutoCompleteModule, ButtonModule, CalendarModule, EditorModule, InputTextModule, PanelModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';

const ENTITY_STATES = [...projectRoute, ...projectPopupRoute];

@NgModule({
  imports: [
    PmAppSharedModule,
    RouterModule.forChild(ENTITY_STATES),
    InputTextModule,
    CalendarModule,
    EditorModule,
    AutoCompleteModule,
    PanelModule,
    TableModule,
    ButtonModule
  ],
  declarations: [
    ProjectComponent,
    ProjectDetailComponent,
    ProjectUpdateComponent,
    ProjectDeleteDialogComponent,
    ProjectDeletePopupComponent,
    ProjectManageComponent
  ],
  entryComponents: [ProjectComponent, ProjectUpdateComponent, ProjectDeleteDialogComponent, ProjectDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppProjectModule {}
