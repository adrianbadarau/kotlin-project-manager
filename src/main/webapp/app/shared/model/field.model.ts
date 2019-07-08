import { IProject } from 'app/shared/model/project.model';
import { IBusinessCase } from 'app/shared/model/business-case.model';
import { IBenefit } from 'app/shared/model/benefit.model';
import { IDelivrable } from 'app/shared/model/delivrable.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface IField {
  id?: string;
  name?: string;
  data?: string;
  projects?: IProject[];
  businessCases?: IBusinessCase[];
  benefits?: IBenefit[];
  delivrables?: IDelivrable[];
  milestones?: IMilestone[];
}

export class Field implements IField {
  constructor(
    public id?: string,
    public name?: string,
    public data?: string,
    public projects?: IProject[],
    public businessCases?: IBusinessCase[],
    public benefits?: IBenefit[],
    public delivrables?: IDelivrable[],
    public milestones?: IMilestone[]
  ) {}
}
