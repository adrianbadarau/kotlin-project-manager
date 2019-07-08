import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  DelivrableComponent,
  DelivrableDetailComponent,
  DelivrableUpdateComponent,
  DelivrableDeletePopupComponent,
  DelivrableDeleteDialogComponent,
  delivrableRoute,
  delivrablePopupRoute
} from './';

const ENTITY_STATES = [...delivrableRoute, ...delivrablePopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DelivrableComponent,
    DelivrableDetailComponent,
    DelivrableUpdateComponent,
    DelivrableDeleteDialogComponent,
    DelivrableDeletePopupComponent
  ],
  entryComponents: [DelivrableComponent, DelivrableUpdateComponent, DelivrableDeleteDialogComponent, DelivrableDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppDelivrableModule {}
