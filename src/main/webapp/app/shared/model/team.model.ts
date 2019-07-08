import { ITask } from 'app/shared/model/task.model';
import { IUser } from 'app/core/user/user.model';
import { IProject } from 'app/shared/model/project.model';

export interface ITeam {
  id?: string;
  name?: string;
  tasks?: ITask[];
  users?: IUser[];
  project?: IProject;
  tasks?: ITask[];
}

export class Team implements ITeam {
  constructor(
    public id?: string,
    public name?: string,
    public tasks?: ITask[],
    public users?: IUser[],
    public project?: IProject,
    public tasks?: ITask[]
  ) {}
}
