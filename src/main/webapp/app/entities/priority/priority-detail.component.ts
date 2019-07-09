import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriority } from 'app/shared/model/priority.model';

@Component({
  selector: 'jhi-priority-detail',
  templateUrl: './priority-detail.component.html'
})
export class PriorityDetailComponent implements OnInit {
  priority: IPriority;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ priority }) => {
      this.priority = priority;
    });
  }

  previousState() {
    window.history.back();
  }
}
