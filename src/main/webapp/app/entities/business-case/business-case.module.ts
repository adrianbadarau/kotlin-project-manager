import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  BusinessCaseComponent,
  BusinessCaseDetailComponent,
  BusinessCaseUpdateComponent,
  BusinessCaseDeletePopupComponent,
  BusinessCaseDeleteDialogComponent,
  businessCaseRoute,
  businessCasePopupRoute
} from './';

const ENTITY_STATES = [...businessCaseRoute, ...businessCasePopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BusinessCaseComponent,
    BusinessCaseDetailComponent,
    BusinessCaseUpdateComponent,
    BusinessCaseDeleteDialogComponent,
    BusinessCaseDeletePopupComponent
  ],
  entryComponents: [
    BusinessCaseComponent,
    BusinessCaseUpdateComponent,
    BusinessCaseDeleteDialogComponent,
    BusinessCaseDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppBusinessCaseModule {}
