import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAttachment } from 'app/shared/model/attachment.model';
import { AccountService } from 'app/core';
import { AttachmentService } from './attachment.service';

@Component({
  selector: 'jhi-attachment',
  templateUrl: './attachment.component.html'
})
export class AttachmentComponent implements OnInit, OnDestroy {
  attachments: IAttachment[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected attachmentService: AttachmentService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.attachmentService
      .query()
      .pipe(
        filter((res: HttpResponse<IAttachment[]>) => res.ok),
        map((res: HttpResponse<IAttachment[]>) => res.body)
      )
      .subscribe(
        (res: IAttachment[]) => {
          this.attachments = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInAttachments();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IAttachment) {
    return item.id;
  }

  registerChangeInAttachments() {
    this.eventSubscriber = this.eventManager.subscribe('attachmentListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
