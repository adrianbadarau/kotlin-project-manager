import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMilestone } from 'app/shared/model/milestone.model';

@Component({
  selector: 'jhi-milestone-detail',
  templateUrl: './milestone-detail.component.html'
})
export class MilestoneDetailComponent implements OnInit {
  milestone: IMilestone;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ milestone }) => {
      this.milestone = milestone;
    });
  }

  previousState() {
    window.history.back();
  }
}
