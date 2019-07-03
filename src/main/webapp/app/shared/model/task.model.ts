import { IMilestone } from 'app/shared/model/milestone.model';
import { IUser } from 'app/core/user/user.model';

export interface ITask {
  id?: number;
  name?: string;
  milestone?: IMilestone;
  users?: IUser[];
}

export class Task implements ITask {
  constructor(public id?: number, public name?: string, public milestone?: IMilestone, public users?: IUser[]) {}
}
