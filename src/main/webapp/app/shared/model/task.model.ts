import { IMilestone } from 'app/shared/model/milestone.model';
import { IStatus } from 'app/shared/model/status.model';
import { ITaskType } from 'app/shared/model/task-type.model';
import { IPriority } from 'app/shared/model/priority.model';
import { IUser } from 'app/core/user/user.model';
import { ITask } from 'app/shared/model/task.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IComment } from 'app/shared/model/comment.model';
import { ITeam } from 'app/shared/model/team.model';

export interface ITask {
  id?: number;
  name?: string;
  description?: string;
  estimatedTime?: number;
  spentTime?: number;
  milestone?: IMilestone;
  status?: IStatus;
  taskType?: ITaskType;
  priority?: IPriority;
  assignee?: IUser;
  parent?: ITask;
  users?: IUser[];
  subTasks?: ITask[];
  attachments?: IAttachment[];
  comments?: IComment[];
  teams?: ITeam[];
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public estimatedTime?: number,
    public spentTime?: number,
    public milestone?: IMilestone,
    public status?: IStatus,
    public taskType?: ITaskType,
    public priority?: IPriority,
    public assignee?: IUser,
    public parent?: ITask,
    public users?: IUser[],
    public subTasks?: ITask[],
    public attachments?: IAttachment[],
    public comments?: IComment[],
    public teams?: ITeam[]
  ) {}
}
