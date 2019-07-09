import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  ChangeHistoryComponent,
  ChangeHistoryDetailComponent,
  ChangeHistoryUpdateComponent,
  ChangeHistoryDeletePopupComponent,
  ChangeHistoryDeleteDialogComponent,
  changeHistoryRoute,
  changeHistoryPopupRoute
} from './';

const ENTITY_STATES = [...changeHistoryRoute, ...changeHistoryPopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChangeHistoryComponent,
    ChangeHistoryDetailComponent,
    ChangeHistoryUpdateComponent,
    ChangeHistoryDeleteDialogComponent,
    ChangeHistoryDeletePopupComponent
  ],
  entryComponents: [
    ChangeHistoryComponent,
    ChangeHistoryUpdateComponent,
    ChangeHistoryDeleteDialogComponent,
    ChangeHistoryDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppChangeHistoryModule {}
