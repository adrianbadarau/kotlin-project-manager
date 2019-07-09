import { IUser } from 'app/core/user/user.model';
import { ITask } from 'app/shared/model/task.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface ITeam {
  id?: string;
  name?: string;
  users?: IUser[];
  tasks?: ITask[];
  milestones?: IMilestone[];
}

export class Team implements ITeam {
  constructor(public id?: string, public name?: string, public users?: IUser[], public tasks?: ITask[], public milestones?: IMilestone[]) {}
}
