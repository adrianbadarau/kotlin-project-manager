import { IBenefitType } from 'app/shared/model/benefit-type.model';
import { IBusinessCase } from 'app/shared/model/business-case.model';

export interface IBenefit {
  id?: string;
  description?: string;
  type?: IBenefitType;
  businessCase?: IBusinessCase;
}

export class Benefit implements IBenefit {
  constructor(public id?: string, public description?: string, public type?: IBenefitType, public businessCase?: IBusinessCase) {}
}
