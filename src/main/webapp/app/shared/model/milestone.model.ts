import { Moment } from 'moment';
import { IProject } from 'app/shared/model/project.model';
import { IField } from 'app/shared/model/field.model';
import { ITeam } from 'app/shared/model/team.model';
import { IUser } from 'app/core/user/user.model';
import { ITask } from 'app/shared/model/task.model';

export interface IMilestone {
  id?: number;
  name?: string;
  estimatedEndDate?: Moment;
  endDate?: Moment;
  project?: IProject;
  fields?: IField[];
  teams?: ITeam[];
  users?: IUser[];
  tasks?: ITask[];
}

export class Milestone implements IMilestone {
  constructor(
    public id?: number,
    public name?: string,
    public estimatedEndDate?: Moment,
    public endDate?: Moment,
    public project?: IProject,
    public fields?: IField[],
    public teams?: ITeam[],
    public users?: IUser[],
    public tasks?: ITask[]
  ) {}
}
