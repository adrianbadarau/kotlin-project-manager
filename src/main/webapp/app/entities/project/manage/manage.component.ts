import { Component, OnInit } from '@angular/core';
import { Project } from 'app/shared/model/project.model';

@Component({
  selector: 'jhi-project-manage',
  templateUrl: './manage.component.html',
  styles: []
})
export class ProjectManageComponent implements OnInit {
  private project: Project;
  results: string[];

  constructor() {}

  ngOnInit() {
    this.project = new Project();
  }

  onClickAssignToMe() {}

  search(event) {
    this.results = ['test', 'testAgain'];
  }
}
