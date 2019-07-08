import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBenefit } from 'app/shared/model/benefit.model';

@Component({
  selector: 'jhi-benefit-detail',
  templateUrl: './benefit-detail.component.html'
})
export class BenefitDetailComponent implements OnInit {
  benefit: IBenefit;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ benefit }) => {
      this.benefit = benefit;
    });
  }

  previousState() {
    window.history.back();
  }
}
