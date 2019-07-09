import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChangeHistory } from 'app/shared/model/change-history.model';

@Component({
  selector: 'jhi-change-history-detail',
  templateUrl: './change-history-detail.component.html'
})
export class ChangeHistoryDetailComponent implements OnInit {
  changeHistory: IChangeHistory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ changeHistory }) => {
      this.changeHistory = changeHistory;
    });
  }

  previousState() {
    window.history.back();
  }
}
