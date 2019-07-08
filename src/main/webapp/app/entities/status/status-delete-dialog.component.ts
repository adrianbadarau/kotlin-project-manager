import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from './status.service';

@Component({
  selector: 'jhi-status-delete-dialog',
  templateUrl: './status-delete-dialog.component.html'
})
export class StatusDeleteDialogComponent {
  status: IStatus;

  constructor(protected statusService: StatusService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.statusService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'statusListModification',
        content: 'Deleted an status'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-status-delete-popup',
  template: ''
})
export class StatusDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ status }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StatusDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.status = status;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/status', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/status', { outlets: { popup: null } }]);
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
