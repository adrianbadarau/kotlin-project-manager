import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        loadChildren: './project/project.module#PmAppProjectModule'
      },
      {
        path: 'business-case',
        loadChildren: './business-case/business-case.module#PmAppBusinessCaseModule'
      },
      {
        path: 'benefit',
        loadChildren: './benefit/benefit.module#PmAppBenefitModule'
      },
      {
        path: 'benefit-type',
        loadChildren: './benefit-type/benefit-type.module#PmAppBenefitTypeModule'
      },
      {
        path: 'milestone',
        loadChildren: './milestone/milestone.module#PmAppMilestoneModule'
      },
      {
        path: 'delivrable',
        loadChildren: './delivrable/delivrable.module#PmAppDelivrableModule'
      },
      {
        path: 'status',
        loadChildren: './status/status.module#PmAppStatusModule'
      },
      {
        path: 'task',
        loadChildren: './task/task.module#PmAppTaskModule'
      },
      {
        path: 'project-update',
        loadChildren: './project-update/project-update.module#PmAppProjectUpdateModule'
      },
      {
        path: 'performance',
        loadChildren: './performance/performance.module#PmAppPerformanceModule'
      },
      {
        path: 'team',
        loadChildren: './team/team.module#PmAppTeamModule'
      },
      {
        path: 'field',
        loadChildren: './field/field.module#PmAppFieldModule'
      },
      {
        path: 'comment',
        loadChildren: './comment/comment.module#PmAppCommentModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PmAppEntityModule {}
