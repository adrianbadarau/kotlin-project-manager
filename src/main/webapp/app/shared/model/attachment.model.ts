import { IProject } from 'app/shared/model/project.model';
import { ITask } from 'app/shared/model/task.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface IAttachment {
  id?: string;
  filename?: string;
  projects?: IProject[];
  tasks?: ITask[];
  milestones?: IMilestone[];
}

export class Attachment implements IAttachment {
  constructor(
    public id?: string,
    public filename?: string,
    public projects?: IProject[],
    public tasks?: ITask[],
    public milestones?: IMilestone[]
  ) {}
}
