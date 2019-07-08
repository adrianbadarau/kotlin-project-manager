import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBenefit } from 'app/shared/model/benefit.model';
import { AccountService } from 'app/core';
import { BenefitService } from './benefit.service';

@Component({
  selector: 'jhi-benefit',
  templateUrl: './benefit.component.html'
})
export class BenefitComponent implements OnInit, OnDestroy {
  benefits: IBenefit[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected benefitService: BenefitService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.benefitService
      .query()
      .pipe(
        filter((res: HttpResponse<IBenefit[]>) => res.ok),
        map((res: HttpResponse<IBenefit[]>) => res.body)
      )
      .subscribe(
        (res: IBenefit[]) => {
          this.benefits = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInBenefits();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBenefit) {
    return item.id;
  }

  registerChangeInBenefits() {
    this.eventSubscriber = this.eventManager.subscribe('benefitListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
