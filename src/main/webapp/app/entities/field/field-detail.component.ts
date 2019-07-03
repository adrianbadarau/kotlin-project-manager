import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IField } from 'app/shared/model/field.model';

@Component({
  selector: 'jhi-field-detail',
  templateUrl: './field-detail.component.html'
})
export class FieldDetailComponent implements OnInit {
  field: IField;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ field }) => {
      this.field = field;
    });
  }

  previousState() {
    window.history.back();
  }
}
