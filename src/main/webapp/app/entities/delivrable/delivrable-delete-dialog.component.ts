import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDelivrable } from 'app/shared/model/delivrable.model';
import { DelivrableService } from './delivrable.service';

@Component({
  selector: 'jhi-delivrable-delete-dialog',
  templateUrl: './delivrable-delete-dialog.component.html'
})
export class DelivrableDeleteDialogComponent {
  delivrable: IDelivrable;

  constructor(
    protected delivrableService: DelivrableService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.delivrableService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'delivrableListModification',
        content: 'Deleted an delivrable'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-delivrable-delete-popup',
  template: ''
})
export class DelivrableDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ delivrable }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DelivrableDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.delivrable = delivrable;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/delivrable', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/delivrable', { outlets: { popup: null } }]);
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
