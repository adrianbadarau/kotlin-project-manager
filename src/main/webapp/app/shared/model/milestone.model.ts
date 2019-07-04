import { Moment } from 'moment';
import { IProject } from 'app/shared/model/project.model';
import { IStatus } from 'app/shared/model/status.model';
import { IField } from 'app/shared/model/field.model';
import { ITeam } from 'app/shared/model/team.model';
import { IUser } from 'app/core/user/user.model';
import { ITask } from 'app/shared/model/task.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IComment } from 'app/shared/model/comment.model';

export interface IMilestone {
  id?: number;
  name?: string;
  estimatedEndDate?: Moment;
  endDate?: Moment;
  project?: IProject;
  status?: IStatus;
  fields?: IField[];
  teams?: ITeam[];
  users?: IUser[];
  tasks?: ITask[];
  attachments?: IAttachment[];
  comments?: IComment[];
}

export class Milestone implements IMilestone {
  constructor(
    public id?: number,
    public name?: string,
    public estimatedEndDate?: Moment,
    public endDate?: Moment,
    public project?: IProject,
    public status?: IStatus,
    public fields?: IField[],
    public teams?: ITeam[],
    public users?: IUser[],
    public tasks?: ITask[],
    public attachments?: IAttachment[],
    public comments?: IComment[]
  ) {}
}
