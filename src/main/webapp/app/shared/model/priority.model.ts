import { ITask } from 'app/shared/model/task.model';

export interface IPriority {
  id?: string;
  name?: string;
  tasks?: ITask[];
}

export class Priority implements IPriority {
  constructor(public id?: string, public name?: string, public tasks?: ITask[]) {}
}
