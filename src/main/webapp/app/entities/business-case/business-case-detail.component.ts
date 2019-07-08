import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusinessCase } from 'app/shared/model/business-case.model';

@Component({
  selector: 'jhi-business-case-detail',
  templateUrl: './business-case-detail.component.html'
})
export class BusinessCaseDetailComponent implements OnInit {
  businessCase: IBusinessCase;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ businessCase }) => {
      this.businessCase = businessCase;
    });
  }

  previousState() {
    window.history.back();
  }
}
