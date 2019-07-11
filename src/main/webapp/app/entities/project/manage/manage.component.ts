import { Component, OnInit } from '@angular/core';
import { Project } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-project-manage',
  templateUrl: './manage.component.html',
  styles: []
})
export class ProjectManageComponent implements OnInit {
  private project: Project;
  results: string[] = ['abadarau', 'testUser'];
  customTables: any[] = [];
  customFields: any[] = [];
  isSaving: boolean;

  constructor(private projectService: ProjectService) {}

  ngOnInit() {
    this.project = new Project();
  }

  onClickAssignToMe() {}

  search(event) {
    this.results = ['test', 'testAgain'];
  }

  onClickAddCustomTable() {
    this.customTables.push([]);
  }

  previousState() {
    window.history.back();
  }

  onSaveButtonClick() {
    this.project.customTables = this.customTables;
    this.project.customFields = this.customFields;
  }
}
