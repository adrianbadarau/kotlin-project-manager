import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IField } from 'app/shared/model/field.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface IProject {
  id?: number;
  name?: string;
  description?: string;
  estimatedEndDate?: Moment;
  owner?: IUser;
  fields?: IField[];
  milestones?: IMilestone[];
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public estimatedEndDate?: Moment,
    public owner?: IUser,
    public fields?: IField[],
    public milestones?: IMilestone[]
  ) {}
}
