import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPerformance } from 'app/shared/model/performance.model';
import { AccountService } from 'app/core';
import { PerformanceService } from './performance.service';

@Component({
  selector: 'jhi-performance',
  templateUrl: './performance.component.html'
})
export class PerformanceComponent implements OnInit, OnDestroy {
  performances: IPerformance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected performanceService: PerformanceService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.performanceService
      .query()
      .pipe(
        filter((res: HttpResponse<IPerformance[]>) => res.ok),
        map((res: HttpResponse<IPerformance[]>) => res.body)
      )
      .subscribe(
        (res: IPerformance[]) => {
          this.performances = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPerformances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPerformance) {
    return item.id;
  }

  registerChangeInPerformances() {
    this.eventSubscriber = this.eventManager.subscribe('performanceListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
