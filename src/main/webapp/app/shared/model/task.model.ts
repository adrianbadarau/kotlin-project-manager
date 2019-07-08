import { Moment } from 'moment';
import { IStatus } from 'app/shared/model/status.model';
import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { IComment } from 'app/shared/model/comment.model';
import { IUser } from 'app/core/user/user.model';
import { ITeam } from 'app/shared/model/team.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface ITask {
  id?: string;
  name?: string;
  estimatedDate?: Moment;
  details?: string;
  status?: IStatus;
  projectUpdates?: IProjectUpdate[];
  comments?: IComment[];
  users?: IUser[];
  teams?: ITeam[];
  assignedTeam?: ITeam;
  milestone?: IMilestone;
}

export class Task implements ITask {
  constructor(
    public id?: string,
    public name?: string,
    public estimatedDate?: Moment,
    public details?: string,
    public status?: IStatus,
    public projectUpdates?: IProjectUpdate[],
    public comments?: IComment[],
    public users?: IUser[],
    public teams?: ITeam[],
    public assignedTeam?: ITeam,
    public milestone?: IMilestone
  ) {}
}
