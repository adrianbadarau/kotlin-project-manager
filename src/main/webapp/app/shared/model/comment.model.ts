import { ITask } from 'app/shared/model/task.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface IComment {
  id?: string;
  body?: string;
  createdAt?: number;
  tasks?: ITask[];
  milestones?: IMilestone[];
}

export class Comment implements IComment {
  constructor(
    public id?: string,
    public body?: string,
    public createdAt?: number,
    public tasks?: ITask[],
    public milestones?: IMilestone[]
  ) {}
}
