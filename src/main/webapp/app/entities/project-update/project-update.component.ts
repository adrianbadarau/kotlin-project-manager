import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProjectUpdate } from 'app/shared/model/project-update.model';
import { AccountService } from 'app/core';
import { ProjectUpdateService } from './project-update.service';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit, OnDestroy {
  projectUpdates: IProjectUpdate[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected projectUpdateService: ProjectUpdateService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.projectUpdateService
      .query()
      .pipe(
        filter((res: HttpResponse<IProjectUpdate[]>) => res.ok),
        map((res: HttpResponse<IProjectUpdate[]>) => res.body)
      )
      .subscribe(
        (res: IProjectUpdate[]) => {
          this.projectUpdates = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInProjectUpdates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProjectUpdate) {
    return item.id;
  }

  registerChangeInProjectUpdates() {
    this.eventSubscriber = this.eventManager.subscribe('projectUpdateListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
