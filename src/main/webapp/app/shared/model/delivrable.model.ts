import { Moment } from 'moment';
import { IMilestone } from 'app/shared/model/milestone.model';
import { IField } from 'app/shared/model/field.model';
import { IProject } from 'app/shared/model/project.model';

export interface IDelivrable {
  id?: string;
  name?: string;
  description?: string;
  target?: Moment;
  milestones?: IMilestone[];
  fields?: IField[];
  project?: IProject;
}

export class Delivrable implements IDelivrable {
  constructor(
    public id?: string,
    public name?: string,
    public description?: string,
    public target?: Moment,
    public milestones?: IMilestone[],
    public fields?: IField[],
    public project?: IProject
  ) {}
}
