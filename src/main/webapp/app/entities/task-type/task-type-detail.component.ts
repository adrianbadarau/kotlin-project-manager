import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskType } from 'app/shared/model/task-type.model';

@Component({
  selector: 'jhi-task-type-detail',
  templateUrl: './task-type-detail.component.html'
})
export class TaskTypeDetailComponent implements OnInit {
  taskType: ITaskType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ taskType }) => {
      this.taskType = taskType;
    });
  }

  previousState() {
    window.history.back();
  }
}
