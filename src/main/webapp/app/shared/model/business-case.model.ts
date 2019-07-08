import { IBenefit } from 'app/shared/model/benefit.model';
import { IField } from 'app/shared/model/field.model';
import { IProject } from 'app/shared/model/project.model';

export interface IBusinessCase {
  id?: string;
  summary?: string;
  benefits?: IBenefit[];
  fields?: IField[];
  project?: IProject;
}

export class BusinessCase implements IBusinessCase {
  constructor(
    public id?: string,
    public summary?: string,
    public benefits?: IBenefit[],
    public fields?: IField[],
    public project?: IProject
  ) {}
}
