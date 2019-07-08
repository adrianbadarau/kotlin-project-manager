import { IMilestone } from 'app/shared/model/milestone.model';
import { ITask } from 'app/shared/model/task.model';

export interface IProjectUpdate {
  id?: string;
  keyMilestoneUpdate?: string;
  comments?: string;
  taskPlan?: string;
  risk?: string;
  supportNeaded?: string;
  milestones?: IMilestone[];
  tasks?: ITask[];
}

export class ProjectUpdate implements IProjectUpdate {
  constructor(
    public id?: string,
    public keyMilestoneUpdate?: string,
    public comments?: string,
    public taskPlan?: string,
    public risk?: string,
    public supportNeaded?: string,
    public milestones?: IMilestone[],
    public tasks?: ITask[]
  ) {}
}
