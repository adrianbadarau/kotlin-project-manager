import { IBenefit } from 'app/shared/model/benefit.model';
import { IProject } from 'app/shared/model/project.model';

export interface IBusinessCase {
  id?: string;
  summary?: string;
  benefits?: IBenefit[];
  project?: IProject;
}

export class BusinessCase implements IBusinessCase {
  constructor(public id?: string, public summary?: string, public benefits?: IBenefit[], public project?: IProject) {}
}
