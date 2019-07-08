import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBenefitType } from 'app/shared/model/benefit-type.model';

@Component({
  selector: 'jhi-benefit-type-detail',
  templateUrl: './benefit-type-detail.component.html'
})
export class BenefitTypeDetailComponent implements OnInit {
  benefitType: IBenefitType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ benefitType }) => {
      this.benefitType = benefitType;
    });
  }

  previousState() {
    window.history.back();
  }
}
