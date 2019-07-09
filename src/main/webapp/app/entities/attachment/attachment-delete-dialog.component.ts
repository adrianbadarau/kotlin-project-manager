import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';

@Component({
  selector: 'jhi-attachment-delete-dialog',
  templateUrl: './attachment-delete-dialog.component.html'
})
export class AttachmentDeleteDialogComponent {
  attachment: IAttachment;

  constructor(
    protected attachmentService: AttachmentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.attachmentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'attachmentListModification',
        content: 'Deleted an attachment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-attachment-delete-popup',
  template: ''
})
export class AttachmentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AttachmentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.attachment = attachment;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/attachment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/attachment', { outlets: { popup: null } }]);
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
