import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IField } from 'app/shared/model/field.model';
import { AccountService } from 'app/core';
import { FieldService } from './field.service';

@Component({
  selector: 'jhi-field',
  templateUrl: './field.component.html'
})
export class FieldComponent implements OnInit, OnDestroy {
  fields: IField[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected fieldService: FieldService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.fieldService
      .query()
      .pipe(
        filter((res: HttpResponse<IField[]>) => res.ok),
        map((res: HttpResponse<IField[]>) => res.body)
      )
      .subscribe(
        (res: IField[]) => {
          this.fields = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFields();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IField) {
    return item.id;
  }

  registerChangeInFields() {
    this.eventSubscriber = this.eventManager.subscribe('fieldListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
