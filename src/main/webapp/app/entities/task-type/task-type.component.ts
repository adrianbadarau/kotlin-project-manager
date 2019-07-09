import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITaskType } from 'app/shared/model/task-type.model';
import { AccountService } from 'app/core';
import { TaskTypeService } from './task-type.service';

@Component({
  selector: 'jhi-task-type',
  templateUrl: './task-type.component.html'
})
export class TaskTypeComponent implements OnInit, OnDestroy {
  taskTypes: ITaskType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected taskTypeService: TaskTypeService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.taskTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<ITaskType[]>) => res.ok),
        map((res: HttpResponse<ITaskType[]>) => res.body)
      )
      .subscribe(
        (res: ITaskType[]) => {
          this.taskTypes = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTaskTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITaskType) {
    return item.id;
  }

  registerChangeInTaskTypes() {
    this.eventSubscriber = this.eventManager.subscribe('taskTypeListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
