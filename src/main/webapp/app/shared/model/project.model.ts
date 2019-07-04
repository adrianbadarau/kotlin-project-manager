import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IStatus } from 'app/shared/model/status.model';
import { IField } from 'app/shared/model/field.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IComment } from 'app/shared/model/comment.model';

export interface IProject {
  id?: number;
  name?: string;
  code?: string;
  description?: string;
  estimatedEndDate?: Moment;
  owner?: IUser;
  status?: IStatus;
  fields?: IField[];
  milestones?: IMilestone[];
  attachments?: IAttachment[];
  comments?: IComment[];
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public code?: string,
    public description?: string,
    public estimatedEndDate?: Moment,
    public owner?: IUser,
    public status?: IStatus,
    public fields?: IField[],
    public milestones?: IMilestone[],
    public attachments?: IAttachment[],
    public comments?: IComment[]
  ) {}
}
