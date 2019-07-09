import { IProject } from 'app/shared/model/project.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { ITask } from 'app/shared/model/task.model';

export interface IComment {
  id?: string;
  body?: string;
  projects?: IProject[];
  milestones?: IMilestone[];
  tasks?: ITask[];
}

export class Comment implements IComment {
  constructor(
    public id?: string,
    public body?: string,
    public projects?: IProject[],
    public milestones?: IMilestone[],
    public tasks?: ITask[]
  ) {}
}
