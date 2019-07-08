import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDelivrable } from 'app/shared/model/delivrable.model';

@Component({
  selector: 'jhi-delivrable-detail',
  templateUrl: './delivrable-detail.component.html'
})
export class DelivrableDetailComponent implements OnInit {
  delivrable: IDelivrable;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ delivrable }) => {
      this.delivrable = delivrable;
    });
  }

  previousState() {
    window.history.back();
  }
}
