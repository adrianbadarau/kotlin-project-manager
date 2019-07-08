import { IMilestone } from 'app/shared/model/milestone.model';
import { ITask } from 'app/shared/model/task.model';

export interface IStatus {
  id?: string;
  name?: string;
  milestones?: IMilestone[];
  tasks?: ITask[];
}

export class Status implements IStatus {
  constructor(public id?: string, public name?: string, public milestones?: IMilestone[], public tasks?: ITask[]) {}
}
