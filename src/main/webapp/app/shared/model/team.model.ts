import { IUser } from 'app/core/user/user.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface ITeam {
  id?: number;
  name?: string;
  users?: IUser[];
  milestones?: IMilestone[];
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public users?: IUser[], public milestones?: IMilestone[]) {}
}
