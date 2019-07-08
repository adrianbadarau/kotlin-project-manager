import { Moment } from 'moment';
import { ITask } from 'app/shared/model/task.model';
import { IStatus } from 'app/shared/model/status.model';
import { IUser } from 'app/core/user/user.model';
import { IField } from 'app/shared/model/field.model';
import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { IPerformance } from 'app/shared/model/performance.model';
import { IComment } from 'app/shared/model/comment.model';
import { IDelivrable } from 'app/shared/model/delivrable.model';

export interface IMilestone {
  id?: string;
  name?: string;
  target?: Moment;
  description?: string;
  workstream?: string;
  code?: string;
  track?: string;
  estimatedEndDate?: Moment;
  actualEndDate?: Moment;
  result?: string;
  tasks?: ITask[];
  status?: IStatus;
  owner?: IUser;
  fields?: IField[];
  projectUpdates?: IProjectUpdate[];
  performances?: IPerformance[];
  comments?: IComment[];
  delivrable?: IDelivrable;
}

export class Milestone implements IMilestone {
  constructor(
    public id?: string,
    public name?: string,
    public target?: Moment,
    public description?: string,
    public workstream?: string,
    public code?: string,
    public track?: string,
    public estimatedEndDate?: Moment,
    public actualEndDate?: Moment,
    public result?: string,
    public tasks?: ITask[],
    public status?: IStatus,
    public owner?: IUser,
    public fields?: IField[],
    public projectUpdates?: IProjectUpdate[],
    public performances?: IPerformance[],
    public comments?: IComment[],
    public delivrable?: IDelivrable
  ) {}
}
