import { IBenefitType } from 'app/shared/model/benefit-type.model';
import { IField } from 'app/shared/model/field.model';
import { IBusinessCase } from 'app/shared/model/business-case.model';

export interface IBenefit {
  id?: string;
  description?: string;
  type?: IBenefitType;
  fields?: IField[];
  businessCase?: IBusinessCase;
}

export class Benefit implements IBenefit {
  constructor(
    public id?: string,
    public description?: string,
    public type?: IBenefitType,
    public fields?: IField[],
    public businessCase?: IBusinessCase
  ) {}
}
