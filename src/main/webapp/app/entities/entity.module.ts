import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        loadChildren: './project/project.module#KotlinPmProjectModule'
      },
      {
        path: 'milestone',
        loadChildren: './milestone/milestone.module#KotlinPmMilestoneModule'
      },
      {
        path: 'task',
        loadChildren: './task/task.module#KotlinPmTaskModule'
      },
      {
        path: 'field',
        loadChildren: './field/field.module#KotlinPmFieldModule'
      },
      {
        path: 'team',
        loadChildren: './team/team.module#KotlinPmTeamModule'
      },
      {
        path: 'project',
        loadChildren: './project/project.module#KotlinPmProjectModule'
      },
      {
        path: 'milestone',
        loadChildren: './milestone/milestone.module#KotlinPmMilestoneModule'
      },
      {
        path: 'task',
        loadChildren: './task/task.module#KotlinPmTaskModule'
      },
      {
        path: 'team',
        loadChildren: './team/team.module#KotlinPmTeamModule'
      },
      {
        path: 'status',
        loadChildren: './status/status.module#KotlinPmStatusModule'
      },
      {
        path: 'attachment',
        loadChildren: './attachment/attachment.module#KotlinPmAttachmentModule'
      },
      {
        path: 'comment',
        loadChildren: './comment/comment.module#KotlinPmCommentModule'
      },
      {
        path: 'task-type',
        loadChildren: './task-type/task-type.module#KotlinPmTaskTypeModule'
      },
      {
        path: 'priority',
        loadChildren: './priority/priority.module#KotlinPmPriorityModule'
      },
      {
        path: 'change-history',
        loadChildren: './change-history/change-history.module#KotlinPmChangeHistoryModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KotlinPmEntityModule {}
