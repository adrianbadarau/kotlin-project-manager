import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PmAppSharedModule } from 'app/shared';
import {
  BenefitTypeComponent,
  BenefitTypeDetailComponent,
  BenefitTypeUpdateComponent,
  BenefitTypeDeletePopupComponent,
  BenefitTypeDeleteDialogComponent,
  benefitTypeRoute,
  benefitTypePopupRoute
} from './';

const ENTITY_STATES = [...benefitTypeRoute, ...benefitTypePopupRoute];

@NgModule({
  imports: [PmAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BenefitTypeComponent,
    BenefitTypeDetailComponent,
    BenefitTypeUpdateComponent,
    BenefitTypeDeleteDialogComponent,
    BenefitTypeDeletePopupComponent
  ],
  entryComponents: [BenefitTypeComponent, BenefitTypeUpdateComponent, BenefitTypeDeleteDialogComponent, BenefitTypeDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppBenefitTypeModule {}
