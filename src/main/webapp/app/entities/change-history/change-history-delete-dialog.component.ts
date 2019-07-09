import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChangeHistory } from 'app/shared/model/change-history.model';
import { ChangeHistoryService } from './change-history.service';

@Component({
  selector: 'jhi-change-history-delete-dialog',
  templateUrl: './change-history-delete-dialog.component.html'
})
export class ChangeHistoryDeleteDialogComponent {
  changeHistory: IChangeHistory;

  constructor(
    protected changeHistoryService: ChangeHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.changeHistoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'changeHistoryListModification',
        content: 'Deleted an changeHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-change-history-delete-popup',
  template: ''
})
export class ChangeHistoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ changeHistory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChangeHistoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.changeHistory = changeHistory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/change-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/change-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
