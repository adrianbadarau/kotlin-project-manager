import { IMilestone } from 'app/shared/model/milestone.model';
import { IProject } from 'app/shared/model/project.model';

export interface IPerformance {
  id?: string;
  timelinePerformance?: number;
  riskRegister?: string;
  mitigationPlan?: string;
  milestones?: IMilestone[];
  projects?: IProject[];
}

export class Performance implements IPerformance {
  constructor(
    public id?: string,
    public timelinePerformance?: number,
    public riskRegister?: string,
    public mitigationPlan?: string,
    public milestones?: IMilestone[],
    public projects?: IProject[]
  ) {}
}
