import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDelivrable } from 'app/shared/model/delivrable.model';
import { AccountService } from 'app/core';
import { DelivrableService } from './delivrable.service';

@Component({
  selector: 'jhi-delivrable',
  templateUrl: './delivrable.component.html'
})
export class DelivrableComponent implements OnInit, OnDestroy {
  delivrables: IDelivrable[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected delivrableService: DelivrableService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.delivrableService
      .query()
      .pipe(
        filter((res: HttpResponse<IDelivrable[]>) => res.ok),
        map((res: HttpResponse<IDelivrable[]>) => res.body)
      )
      .subscribe(
        (res: IDelivrable[]) => {
          this.delivrables = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDelivrables();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDelivrable) {
    return item.id;
  }

  registerChangeInDelivrables() {
    this.eventSubscriber = this.eventManager.subscribe('delivrableListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
