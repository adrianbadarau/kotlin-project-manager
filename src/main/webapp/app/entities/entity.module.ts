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
