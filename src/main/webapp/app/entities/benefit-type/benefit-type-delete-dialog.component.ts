import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBenefitType } from 'app/shared/model/benefit-type.model';
import { BenefitTypeService } from './benefit-type.service';

@Component({
  selector: 'jhi-benefit-type-delete-dialog',
  templateUrl: './benefit-type-delete-dialog.component.html'
})
export class BenefitTypeDeleteDialogComponent {
  benefitType: IBenefitType;

  constructor(
    protected benefitTypeService: BenefitTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.benefitTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'benefitTypeListModification',
        content: 'Deleted an benefitType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-benefit-type-delete-popup',
  template: ''
})
export class BenefitTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ benefitType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BenefitTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.benefitType = benefitType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/benefit-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/benefit-type', { outlets: { popup: null } }]);
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
