import { Moment } from 'moment';
import { IBusinessCase } from 'app/shared/model/business-case.model';
import { ITeam } from 'app/shared/model/team.model';
import { IDelivrable } from 'app/shared/model/delivrable.model';
import { IField } from 'app/shared/model/field.model';
import { IPerformance } from 'app/shared/model/performance.model';

export interface IProject {
  id?: string;
  name?: string;
  objective?: string;
  target?: Moment;
  budget?: number;
  risk?: string;
  benefitMesurement?: string;
  businessCase?: IBusinessCase;
  teams?: ITeam[];
  delivrables?: IDelivrable[];
  fields?: IField[];
  performances?: IPerformance[];
}

export class Project implements IProject {
  constructor(
    public id?: string,
    public name?: string,
    public objective?: string,
    public target?: Moment,
    public budget?: number,
    public risk?: string,
    public benefitMesurement?: string,
    public businessCase?: IBusinessCase,
    public teams?: ITeam[],
    public delivrables?: IDelivrable[],
    public fields?: IField[],
    public performances?: IPerformance[]
  ) {}
}
