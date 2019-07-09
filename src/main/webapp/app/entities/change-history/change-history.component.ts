import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IChangeHistory } from 'app/shared/model/change-history.model';
import { AccountService } from 'app/core';
import { ChangeHistoryService } from './change-history.service';

@Component({
  selector: 'jhi-change-history',
  templateUrl: './change-history.component.html'
})
export class ChangeHistoryComponent implements OnInit, OnDestroy {
  changeHistories: IChangeHistory[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected changeHistoryService: ChangeHistoryService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.changeHistoryService
      .query()
      .pipe(
        filter((res: HttpResponse<IChangeHistory[]>) => res.ok),
        map((res: HttpResponse<IChangeHistory[]>) => res.body)
      )
      .subscribe(
        (res: IChangeHistory[]) => {
          this.changeHistories = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInChangeHistories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IChangeHistory) {
    return item.id;
  }

  registerChangeInChangeHistories() {
    this.eventSubscriber = this.eventManager.subscribe('changeHistoryListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
