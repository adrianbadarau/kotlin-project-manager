import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
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
  currentSearch: string;

  constructor(
    protected fieldService: FieldService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.fieldService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IField[]>) => res.ok),
          map((res: HttpResponse<IField[]>) => res.body)
        )
        .subscribe((res: IField[]) => (this.fields = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.fieldService
      .query()
      .pipe(
        filter((res: HttpResponse<IField[]>) => res.ok),
        map((res: HttpResponse<IField[]>) => res.body)
      )
      .subscribe(
        (res: IField[]) => {
          this.fields = res;
          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
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
