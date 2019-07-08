import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBenefit } from 'app/shared/model/benefit.model';
import { BenefitService } from './benefit.service';

@Component({
  selector: 'jhi-benefit-delete-dialog',
  templateUrl: './benefit-delete-dialog.component.html'
})
export class BenefitDeleteDialogComponent {
  benefit: IBenefit;

  constructor(protected benefitService: BenefitService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.benefitService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'benefitListModification',
        content: 'Deleted an benefit'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-benefit-delete-popup',
  template: ''
})
export class BenefitDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ benefit }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BenefitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.benefit = benefit;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/benefit', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/benefit', { outlets: { popup: null } }]);
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
