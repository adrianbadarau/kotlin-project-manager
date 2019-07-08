import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IField } from 'app/shared/model/field.model';
import { FieldService } from './field.service';

@Component({
  selector: 'jhi-field-delete-dialog',
  templateUrl: './field-delete-dialog.component.html'
})
export class FieldDeleteDialogComponent {
  field: IField;

  constructor(protected fieldService: FieldService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.fieldService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'fieldListModification',
        content: 'Deleted an field'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-field-delete-popup',
  template: ''
})
export class FieldDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ field }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FieldDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.field = field;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/field', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/field', { outlets: { popup: null } }]);
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
