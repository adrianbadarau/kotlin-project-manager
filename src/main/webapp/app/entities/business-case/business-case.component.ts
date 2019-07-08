import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBusinessCase } from 'app/shared/model/business-case.model';
import { AccountService } from 'app/core';
import { BusinessCaseService } from './business-case.service';

@Component({
  selector: 'jhi-business-case',
  templateUrl: './business-case.component.html'
})
export class BusinessCaseComponent implements OnInit, OnDestroy {
  businessCases: IBusinessCase[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected businessCaseService: BusinessCaseService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.businessCaseService
      .query()
      .pipe(
        filter((res: HttpResponse<IBusinessCase[]>) => res.ok),
        map((res: HttpResponse<IBusinessCase[]>) => res.body)
      )
      .subscribe(
        (res: IBusinessCase[]) => {
          this.businessCases = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInBusinessCases();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBusinessCase) {
    return item.id;
  }

  registerChangeInBusinessCases() {
    this.eventSubscriber = this.eventManager.subscribe('businessCaseListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
