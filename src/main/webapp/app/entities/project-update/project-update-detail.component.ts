import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectUpdate } from 'app/shared/model/project-update.model';

@Component({
  selector: 'jhi-project-update-detail',
  templateUrl: './project-update-detail.component.html'
})
export class ProjectUpdateDetailComponent implements OnInit {
  projectUpdate: IProjectUpdate;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ projectUpdate }) => {
      this.projectUpdate = projectUpdate;
    });
  }

  previousState() {
    window.history.back();
  }
}
