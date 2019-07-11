import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IStatus } from 'app/shared/model/status.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { IAttachment } from 'app/shared/model/attachment.model';
import { IComment } from 'app/shared/model/comment.model';
import { IField } from 'app/shared/model/field.model';

export interface IProject {
  id?: string;
  name?: string;
  code?: string;
  description?: string;
  estimatedEndDate?: Moment;
  owner?: IUser;
  status?: IStatus;
  milestones?: IMilestone[];
  attachments?: IAttachment[];
  comments?: IComment[];
  customFields?: IField[];
  customTables?: any[];
}

export class Project implements IProject {
  constructor(
    public id?: string,
    public name?: string,
    public code?: string,
    public description?: string,
    public estimatedEndDate?: Moment,
    public owner?: IUser,
    public status?: IStatus,
    public milestones?: IMilestone[],
    public attachments?: IAttachment[],
    public comments?: IComment[],
    public customFields?: IField[],
    public customTables?: any[]
  ) {}
}
