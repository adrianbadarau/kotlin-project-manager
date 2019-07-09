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
        path: 'milestone',
        loadChildren: './milestone/milestone.module#PmAppMilestoneModule'
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
        path: 'team',
        loadChildren: './team/team.module#PmAppTeamModule'
      },
      {
        path: 'comment',
        loadChildren: './comment/comment.module#PmAppCommentModule'
      },
      {
        path: 'attachment',
        loadChildren: './attachment/attachment.module#PmAppAttachmentModule'
      },
      {
        path: 'task-type',
        loadChildren: './task-type/task-type.module#PmAppTaskTypeModule'
      },
      {
        path: 'priority',
        loadChildren: './priority/priority.module#PmAppPriorityModule'
      },
      {
        path: 'change-history',
        loadChildren: './change-history/change-history.module#PmAppChangeHistoryModule'
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
