import { ITask } from 'app/shared/model/task.model';

export interface ITaskType {
  id?: string;
  name?: string;
  tasks?: ITask[];
}

export class TaskType implements ITaskType {
  constructor(public id?: string, public name?: string, public tasks?: ITask[]) {}
}
