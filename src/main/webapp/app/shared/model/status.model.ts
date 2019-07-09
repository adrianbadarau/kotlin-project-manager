import { IProject } from 'app/shared/model/project.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { ITask } from 'app/shared/model/task.model';

export interface IStatus {
  id?: string;
  name?: string;
  projects?: IProject[];
  milestones?: IMilestone[];
  tasks?: ITask[];
}

export class Status implements IStatus {
  constructor(
    public id?: string,
    public name?: string,
    public projects?: IProject[],
    public milestones?: IMilestone[],
    public tasks?: ITask[]
  ) {}
}
