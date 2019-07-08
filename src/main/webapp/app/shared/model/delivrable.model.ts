import { Moment } from 'moment';
import { IMilestone } from 'app/shared/model/milestone.model';
import { IProject } from 'app/shared/model/project.model';

export interface IDelivrable {
  id?: string;
  name?: string;
  description?: string;
  target?: Moment;
  milestones?: IMilestone[];
  project?: IProject;
}

export class Delivrable implements IDelivrable {
  constructor(
    public id?: string,
    public name?: string,
    public description?: string,
    public target?: Moment,
    public milestones?: IMilestone[],
    public project?: IProject
  ) {}
}
