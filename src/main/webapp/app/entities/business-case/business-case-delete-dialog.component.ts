import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusinessCase } from 'app/shared/model/business-case.model';
import { BusinessCaseService } from './business-case.service';

@Component({
  selector: 'jhi-business-case-delete-dialog',
  templateUrl: './business-case-delete-dialog.component.html'
})
export class BusinessCaseDeleteDialogComponent {
  businessCase: IBusinessCase;

  constructor(
    protected businessCaseService: BusinessCaseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.businessCaseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'businessCaseListModification',
        content: 'Deleted an businessCase'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-business-case-delete-popup',
  template: ''
})
export class BusinessCaseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ businessCase }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BusinessCaseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.businessCase = businessCase;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/business-case', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/business-case', { outlets: { popup: null } }]);
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
