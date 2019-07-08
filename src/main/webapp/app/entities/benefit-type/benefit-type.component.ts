import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBenefitType } from 'app/shared/model/benefit-type.model';
import { AccountService } from 'app/core';
import { BenefitTypeService } from './benefit-type.service';

@Component({
  selector: 'jhi-benefit-type',
  templateUrl: './benefit-type.component.html'
})
export class BenefitTypeComponent implements OnInit, OnDestroy {
  benefitTypes: IBenefitType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected benefitTypeService: BenefitTypeService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.benefitTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<IBenefitType[]>) => res.ok),
        map((res: HttpResponse<IBenefitType[]>) => res.body)
      )
      .subscribe(
        (res: IBenefitType[]) => {
          this.benefitTypes = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInBenefitTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBenefitType) {
    return item.id;
  }

  registerChangeInBenefitTypes() {
    this.eventSubscriber = this.eventManager.subscribe('benefitTypeListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
