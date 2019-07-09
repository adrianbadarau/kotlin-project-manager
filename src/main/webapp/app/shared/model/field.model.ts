import { IProject } from 'app/shared/model/project.model';
import { IMilestone } from 'app/shared/model/milestone.model';

export interface IField {
  id?: string;
  name?: string;
  data?: string;
  projects?: IProject[];
  milestones?: IMilestone[];
}

export class Field implements IField {
  constructor(
    public id?: string,
    public name?: string,
    public data?: string,
    public projects?: IProject[],
    public milestones?: IMilestone[]
  ) {}
}
